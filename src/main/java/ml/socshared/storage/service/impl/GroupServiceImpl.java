package ml.socshared.storage.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ml.socshared.storage.domain.model.GroupModel;
import ml.socshared.storage.domain.request.GroupRequest;
import ml.socshared.storage.domain.response.GroupResponse;
import ml.socshared.storage.entity.Group;
import ml.socshared.storage.exception.impl.GroupIsAlreadyConnectedException;
import ml.socshared.storage.exception.impl.HttpNotFoundException;
import ml.socshared.storage.repository.GroupRepository;
import ml.socshared.storage.service.GroupService;
import ml.socshared.storage.service.sentry.SentrySender;
import ml.socshared.storage.service.sentry.SentryTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final SentrySender sentrySender;

    @Override
    public GroupResponse save(GroupRequest request) {
        log.info("saving -> {}", request);

        boolean groupVkIsConnect = groupRepository.findDistinctTopByUserIdAndVkId(UUID.fromString(request.getUserId()), request.getVkId()).orElse(null) != null;
        boolean groupFbIsConnect = groupRepository.findDistinctTopByUserIdAndFacebookId(UUID.fromString(request.getUserId()), request.getFbId()).orElse(null) != null;

        if (groupFbIsConnect && request.getSocialNetwork() == Group.SocialNetwork.FACEBOOK)
            throw new GroupIsAlreadyConnectedException(String.format("Group FB (%s) is already connected.", request.getFbId()));

        if (groupVkIsConnect && request.getSocialNetwork() == Group.SocialNetwork.VK)
            throw new GroupIsAlreadyConnectedException(String.format("Group VK (%s) is already connected.", request.getVkId()));

        Group group = new Group();
        group.setUserId(UUID.fromString(request.getUserId()));
        group.setName(request.getName());
        group.setSocialNetwork(request.getSocialNetwork());

        switch (request.getSocialNetwork()) {
            case FACEBOOK:
                group.setGroupFacebookId(request.getFbId());
                break;
            case VK:
                group.setGroupVkId(request.getVkId());
                break;
        }

        GroupResponse response = new GroupResponse(groupRepository.save(group));

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("group", response);
        sentrySender.sentryMessage("save group", additionalData, Collections.singletonList(SentryTag.SAVE_GROUP));

        return response;
    }

    @Override
    public void deleteById(UUID groupId) {
        log.info("removing by group id -> {}", groupId);
        groupRepository.deleteById(groupId);

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("group_id", groupId);
        sentrySender.sentryMessage("delete group by group id " + groupId, additionalData, Collections.singletonList(SentryTag.DELETE_GROUP_BY_ID));
    }

    @Override
    public void deleteByVkId(UUID userId, String vkId) {
        log.info("removing by vk id -> {}", vkId);
        groupRepository.findDistinctTopByUserIdAndVkId(userId, vkId).ifPresent(groupRepository::delete);

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("system_user_id", userId);
        additionalData.put("vk_group_id", vkId);
        sentrySender.sentryMessage("delete group by vk group id " + vkId, additionalData, Collections.singletonList(SentryTag.DELETE_GROUP_BY_VK_ID));
    }

    @Override
    public void deleteByFbId(UUID userId, String fbId) {
        log.info("removing by fb id -> {}", fbId);
        groupRepository.findDistinctTopByUserIdAndFacebookId(userId, fbId).ifPresent(groupRepository::delete);

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("system_user_id", userId);
        additionalData.put("fb_group_id", fbId);
        sentrySender.sentryMessage("delete group by fb group id " + fbId, additionalData, Collections.singletonList(SentryTag.DELETE_GROUP_BY_FB_ID));
    }

    @Override
    public GroupResponse findById(UUID groupId) {
        log.info("found by id -> {}", groupId);

        GroupResponse groupResponse = new GroupResponse(groupRepository.findById(groupId)
                .orElseThrow(() -> new HttpNotFoundException("Not found group by id")));

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("group_id", groupId);
        sentrySender.sentryMessage("get group by group id " + groupId, additionalData,
                Collections.singletonList(SentryTag.GET_GROUP_BY_ID));

        return groupResponse;
    }

    @Override
    public Page<GroupModel> findByUserId(UUID userId, Integer page, Integer size) {
        log.info("found by user id -> {}", userId);

        Page<GroupModel> result = groupRepository.findByUserId(userId, PageRequest.of(page, size));

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("system_user_id", userId);
        sentrySender.sentryMessage("get groups by user id " + userId, additionalData,
                Collections.singletonList(SentryTag.GET_GROUPS_BY_USER_ID));

        return result;
    }

    @Override
    public Page<GroupModel> findByUserIdAndSocialNetwork(UUID userId, Group.SocialNetwork socialNetwork, Integer page, Integer size) {
        log.info("found by user id and social network -> {}, {}", userId, socialNetwork);

        Page<GroupModel> result = groupRepository.findByUserIdAndSocialNetwork(userId, socialNetwork, PageRequest.of(page, size));

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("system_user_id", userId);
        additionalData.put("social_network", socialNetwork);
        sentrySender.sentryMessage("get groups by user id " + userId + " and social network " + socialNetwork,
                additionalData, Collections.singletonList(SentryTag.GET_GROUPS_BY_USER_ID_AND_SOCIAL_NETWORK));

        return result;
    }

    @Override
    public GroupResponse findByUserIdAndVkId(UUID userId, String vkId) {
        log.info("found by user id and vk id -> {}, {}", userId, vkId);

        GroupResponse response = new GroupResponse(groupRepository.findDistinctTopByUserIdAndVkId(userId, vkId)
                .orElseThrow(() -> new HttpNotFoundException("Not found group by user id and vk id")));

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("system_user_id", userId);
        additionalData.put("vk_id", vkId);
        sentrySender.sentryMessage("get groups by user id " + userId + " and vk id " +vkId,
                additionalData, Collections.singletonList(SentryTag.GET_BY_USER_ID_AND_VK_ID));

        return response;
    }

    @Override
    public GroupResponse findByUserIdAndFacebookId(UUID userId, String facebookId) {
        log.info("found by user id and facebook id -> {}, {}", userId, facebookId);

        GroupResponse response = new GroupResponse(groupRepository.findDistinctTopByUserIdAndFacebookId(userId, facebookId)
                .orElseThrow(() -> new HttpNotFoundException("Not found group by user id and facebook id")));

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("system_user_id", userId);
        additionalData.put("facebook_id", facebookId);
        sentrySender.sentryMessage("get groups by user id " + userId + " and facebook id " + facebookId,
                additionalData, Collections.singletonList(SentryTag.GET_BY_USER_ID_AND_FACEBOOK_ID));

        return response;
    }

    @Override
    public void deleteVkGroupsByUserId(UUID userId) {
        log.info("removing groups vk by user id -> {}", userId);
        groupRepository.deleteByUserIdAndSocialNetwork(userId, Group.SocialNetwork.VK);

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("system_user_id", userId);
        sentrySender.sentryMessage("delete vk groups by user id " + userId,
                additionalData, Collections.singletonList(SentryTag.DELETE_VK_GROUPS_BY_USER_ID));
    }

    @Override
    public void deleteFacebookGroupsByUserId(UUID userId) {
        log.info("removing groups facebook by user id -> {}", userId);
        groupRepository.deleteByUserIdAndSocialNetwork(userId, Group.SocialNetwork.FACEBOOK);

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("system_user_id", userId);
        sentrySender.sentryMessage("delete facebook groups by user id " + userId,
                additionalData, Collections.singletonList(SentryTag.DELETE_FACEBOOK_GROUPS_BY_USER_ID));
    }
}
