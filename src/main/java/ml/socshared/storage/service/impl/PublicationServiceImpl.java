package ml.socshared.storage.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ml.socshared.storage.domain.request.PublicationRequest;
import ml.socshared.storage.domain.response.PublicationResponse;
import ml.socshared.storage.entity.Group;
import ml.socshared.storage.entity.GroupPostStatus;
import ml.socshared.storage.entity.Publication;
import ml.socshared.storage.exception.impl.HttpNotFoundException;
import ml.socshared.storage.repository.GroupRepository;
import ml.socshared.storage.repository.PublicationRepository;
import ml.socshared.storage.service.PublicationService;
import ml.socshared.storage.service.sentry.SentrySender;
import ml.socshared.storage.service.sentry.SentryTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicationServiceImpl implements PublicationService {

    private final GroupRepository groupRepository;
    private final PublicationRepository publicationRepository;
    private final SentrySender sentrySender;

    @Override
    public PublicationResponse save(PublicationRequest request) {
        log.info("saving -> {}", request);

        Publication publication = request.getPublicationId() != null ?
                publicationRepository.findById(UUID.fromString(request.getPublicationId())).orElse(new Publication()) : new Publication();
        if (request.getPublicationDateTime() != null) {
            publication.setPublicationDateTime(request.getPublicationDateTime());
        }
        publication.setUserId(UUID.fromString(request.getUserId()));
        publication.setPostType(request.getType());
        publication.setText(request.getText());

        Set<Group> groupSet = publication.getGroups() != null ? publication.getGroups() : new HashSet<>();

        Set<GroupPostStatus> groupPostStatuses = publication.getPostStatus() != null ? publication.getPostStatus() : new HashSet<>();
        String[] groupIds = request.getGroupIds();
        for (String groupId : groupIds) {
            Group group = groupRepository.findById(UUID.fromString(groupId)).orElseThrow(() -> new HttpNotFoundException("Not found group by id: " + groupId));
            groupSet.add(group);
            GroupPostStatus result = null;
            for (GroupPostStatus status : groupPostStatuses) {
                if (status.getGroupId().equals(UUID.fromString(groupId))) {
                    result = status;
                    break;
                }
            }
            groupPostStatuses.remove(result);
            if (result == null) {
                result = new GroupPostStatus();
                result.setGroupId(UUID.fromString(groupId));
                result.setGroupFacebookId(group.getFacebookId());
                result.setGroupVkId(group.getVkId());
                result.setSocialNetwork(group.getSocialNetwork());
                result.setPostFacebookId(request.getPostFacebookId());
                result.setPostVkId(request.getPostVkId());
                result.setPublication(publication);
            }
            result.setPostStatus(request.getPostStatus() != null ? request.getPostStatus() : GroupPostStatus.PostStatus.AWAITING);
            result.setStatusText(request.getStatusText());
            groupPostStatuses.add(result);
        }
        publication.setGroups(groupSet);
        publication.setPostStatus(groupPostStatuses);

        PublicationResponse response = new PublicationResponse(publicationRepository.save(publication));

        Map<String, Object> additionData = new HashMap<>();
        additionData.put("publication", response);
        sentrySender.sentryMessage("save publication", additionData, Collections.singletonList(SentryTag.SAVE_PUBLICATION));

        return response;
    }

    @Override
    public Page<Publication> findNotPublishing(Integer page, Integer size) {
        log.info("find not publishing");

        Page<Publication> result = publicationRepository.findNotPublishing(PageRequest.of(page, size));

        Map<String, Object> additionData = new HashMap<>();
        sentrySender.sentryMessage("get publications not publishing", additionData, Collections.singletonList(SentryTag.GET_NOT_PUBLISHING_PUBLICATIONS));

        return result;
    }

    @Override
    public Page<Publication> findPublishingAfter(Long date, Integer page, Integer size) {
        log.info("find publications after");
        Date d = new Date(date);

        Page<Publication> result = publicationRepository.findByPublishingAfter(d, PageRequest.of(page, size));

        Map<String, Object> additionData = new HashMap<>();
        sentrySender.sentryMessage("get publications after " + d.getDay() + "." + d.getMonth() + "." + d.getYear() + " "
                + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds(),
                additionData, Collections.singletonList(SentryTag.GET_PUBLISHING_AFTER_PUBLICATIONS));

        return result;
    }

    @Override
    public Page<Publication> findByGroupId(UUID systemGroupId, Integer page, Integer size) {
        log.info("find publication by user id");
        Pageable pageable = PageRequest.of(page, size);

        Page<Publication> result = publicationRepository.findByGroupId(systemGroupId, pageable);

        Map<String, Object> additionData = new HashMap<>();
        sentrySender.sentryMessage("get publications by group id " + systemGroupId, additionData, Collections.singletonList(SentryTag.GET_PUBLICATIONS_BY_GROUP_ID));

        return result;
    }
}
