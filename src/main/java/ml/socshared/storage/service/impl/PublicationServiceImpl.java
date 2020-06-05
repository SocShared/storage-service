package ml.socshared.storage.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ml.socshared.storage.domain.model.PublicationModel;
import ml.socshared.storage.domain.request.PublicationRequest;
import ml.socshared.storage.domain.response.PublicationResponse;
import ml.socshared.storage.entity.Group;
import ml.socshared.storage.entity.GroupPostStatus;
import ml.socshared.storage.entity.Publication;
import ml.socshared.storage.exception.impl.HttpNotFoundException;
import ml.socshared.storage.exception.impl.IncorrectDateException;
import ml.socshared.storage.repository.GroupRepository;
import ml.socshared.storage.repository.PublicationRepository;
import ml.socshared.storage.service.PublicationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicationServiceImpl implements PublicationService {

    private final GroupRepository groupRepository;
    private final PublicationRepository publicationRepository;

    @Override
    public PublicationResponse save(PublicationRequest request) {
        log.info("saving -> {}", request);

        Publication publication = request.getPublicationId() != null ?
                publicationRepository.findById(UUID.fromString(request.getPublicationId())).orElse(new Publication()) : new Publication();
        if (request.getPublicationDateTime() != null) {
            if (request.getPublicationDateTime().before(new Date()))
                throw new IncorrectDateException("date must be later than the current date");
            publication.setPublicationDateTime(request.getPublicationDateTime());
        }
        publication.setUserId(UUID.fromString(request.getUserId()));
        publication.setPostType(request.getType());
        publication.setText(request.getText());

        Set<Group> groupSet = publication.getGroups() != null ? publication.getGroups() : new HashSet<>();

        Set<GroupPostStatus> groupPostStatuses = publication.getPostStatus() != null ? publication.getPostStatus() : new HashSet<>();
        String[] groupIds = request.getGroupIds();
        for (String groupId : groupIds) {
            groupSet.add(groupRepository.findById(UUID.fromString(groupId)).orElseThrow(() -> new HttpNotFoundException("Not found group by id: " + groupId)));
            GroupPostStatus result = null;
            for (GroupPostStatus status : groupPostStatuses) {
                if (status.getGroupId().equals(UUID.fromString(groupId))) {
                    result = status;
                    break;
                }
            }
            groupPostStatuses.remove(result);
            if (result != null) {
                result.setPostStatus(request.getPostStatus() != null ? request.getPostStatus() : GroupPostStatus.PostStatus.AWAITING);
            } else {
                result = new GroupPostStatus();
                result.setPostStatus(request.getPostStatus() != null ? request.getPostStatus() : GroupPostStatus.PostStatus.AWAITING);
                result.setGroupId(UUID.fromString(groupId));
                result.setPublication(publication);
            }
            groupPostStatuses.add(result);
        }
        publication.setGroups(groupSet);
        publication.setPostStatus(groupPostStatuses);

        return new PublicationResponse(publicationRepository.save(publication));
    }

    @Override
    public Page<PublicationModel> findNotPublishing(Integer page, Integer size) {
        log.info("find not publishing");
        return publicationRepository.findNotPublishing(PageRequest.of(page, size));
    }

    @Override
    public Page<PublicationModel> findPublishingAfter(Long date, Integer page, Integer size) {
        log.info("find publications after");
        Date d = new Date(date);
        return publicationRepository.findPublishingAfter(d, PageRequest.of(page, size));
    }

    @Override
    public Page<PublicationModel> findByGroupId(UUID systemGroupId, Integer page, Integer size) {
        log.info("find publication by user id");
        Optional<Group> g = groupRepository.findById(systemGroupId);
        if(g.isEmpty()) {
            throw new HttpNotFoundException("group by id " + systemGroupId + "not found");
        }
        Pageable pageable = PageRequest.of(page, size);
        return publicationRepository.findByGroups(g.get(), pageable);
    }
}
