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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

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
                group.setFacebookId(request.getFbId());
                break;
            case VK:
                group.setVkId(request.getVkId());
                break;
        }

        return new GroupResponse(groupRepository.save(group));
    }

    @Override
    public void deleteById(UUID groupId) {
        log.info("removing by group id -> {}", groupId);
        groupRepository.deleteById(groupId);
    }

    @Override
    public void deleteByVkId(UUID userId, String vkId) {
        log.info("removing by vk id -> {}", vkId);
        groupRepository.findDistinctTopByUserIdAndVkId(userId, vkId).ifPresent(groupRepository::delete);
    }

    @Override
    public void deleteByFbId(UUID userId, String fbId) {
        log.info("removing by fb id -> {}", fbId);
        groupRepository.findDistinctTopByUserIdAndFacebookId(userId, fbId).ifPresent(groupRepository::delete);
    }

    @Override
    public GroupResponse findById(UUID groupId) {
        log.info("found by id -> {}", groupId);
        return new GroupResponse(groupRepository.findById(groupId)
                .orElseThrow(() -> new HttpNotFoundException("Not found group by id")));
    }

    @Override
    public Page<GroupModel> findByUserId(UUID userId, Integer page, Integer size) {
        log.info("found by user id -> {}", userId);
        return groupRepository.findByUserId(userId, PageRequest.of(page, size));
    }

    @Override
    public Page<GroupModel> findByUserIdAndSocialNetwork(UUID userId, Group.SocialNetwork socialNetwork, Integer page, Integer size) {
        log.info("found by user id and social network -> {}, {}", userId, socialNetwork);
        return groupRepository.findByUserIdAndSocialNetwork(userId, socialNetwork, PageRequest.of(page, size));
    }

    @Override
    public GroupResponse findByUserIdAndVkId(UUID userId, String vkId) {
        log.info("found by user id and vk id -> {}, {}", userId, vkId);
        return new GroupResponse(groupRepository.findDistinctTopByUserIdAndVkId(userId, vkId)
                .orElseThrow(() -> new HttpNotFoundException("Not found group by user id and vk id")));
    }

    @Override
    public GroupResponse findByUserIdAndFacebookId(UUID userId, String facebookId) {
        log.info("found by user id and facebook id -> {}, {}", userId, facebookId);
        return new GroupResponse(groupRepository.findDistinctTopByUserIdAndFacebookId(userId, facebookId)
                .orElseThrow(() -> new HttpNotFoundException("Not found group by user id and facebook id")));
    }
}
