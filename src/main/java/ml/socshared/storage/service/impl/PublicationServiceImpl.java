package ml.socshared.storage.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ml.socshared.storage.config.RabbitMQConfig;
import ml.socshared.storage.domain.request.PublicationRequest;
import ml.socshared.storage.domain.response.PublicationCountResponse;
import ml.socshared.storage.domain.response.PublicationResponse;
import ml.socshared.storage.entity.Group;
import ml.socshared.storage.entity.GroupPostStatus;
import ml.socshared.storage.entity.Publication;
import ml.socshared.storage.exception.impl.HttpNotFoundException;
import ml.socshared.storage.repository.GroupPostStatusRepository;
import ml.socshared.storage.repository.GroupRepository;
import ml.socshared.storage.repository.PublicationRepository;
import ml.socshared.storage.service.PublicationService;
import ml.socshared.storage.service.sentry.SentrySender;
import ml.socshared.storage.service.sentry.SentryTag;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicationServiceImpl implements PublicationService {

    private final GroupRepository groupRepository;
    private final GroupPostStatusRepository groupPostStatusRepository;
    private final PublicationRepository publicationRepository;
    private final SentrySender sentrySender;

    private final RabbitTemplate rabbitTemplate;

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

        Set<GroupPostStatus> groupPostStatuses = publication.getPostStatus() != null ? publication.getPostStatus() : new HashSet<>();

        String[] groupIds = request.getGroupIds();
        Publication publicationSave = publicationRepository.save(publication);
        for (String groupId : groupIds) {
            Group group = groupRepository.findById(UUID.fromString(groupId)).orElseThrow(() -> new HttpNotFoundException("Not found group by id: " + groupId));
            GroupPostStatus result = new GroupPostStatus();
            result.setStatusText(request.getStatusText());
            result.setGroupId(group.getGroupId());
            result.setPostStatus(request.getPostStatus() != null ? request.getPostStatus() : GroupPostStatus.PostStatus.AWAITING);
            result.setPublicationId(publicationSave.getPublicationId());
            result.setPostVkId(request.getPostVkId());
            result.setPostFacebookId(request.getPostFacebookId());
            result.setSocialNetwork(group.getSocialNetwork());
            result.setGroupVkId(group.getGroupVkId());
            result.setGroupFacebookId(group.getGroupFacebookId());
            result = groupPostStatusRepository.save(result);
            groupPostStatuses.add(result);
        }
        publicationSave.setPostStatus(groupPostStatuses);

        PublicationResponse response = new PublicationResponse(publicationSave);

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
        additionData.put("datetime", new Date(date));
        sentrySender.sentryMessage("get publications after <datetime>",
                additionData, Collections.singletonList(SentryTag.GET_PUBLISHING_AFTER_PUBLICATIONS));

        return result;
    }

    @Override
    public Page<Publication> findByGroupId(UUID systemGroupId, Integer page, Integer size) {
        log.info("find publication by user id");
        Pageable pageable = PageRequest.of(page, size);

        Page<Publication> result = publicationRepository.findByGroupId(systemGroupId, pageable);

        Map<String, Object> additionData = new HashMap<>();
        sentrySender.sentryMessage("get publications by group id", additionData, Collections.singletonList(SentryTag.GET_PUBLICATIONS_BY_GROUP_ID));

        return result;
    }

    @Override
    public PublicationCountResponse publicationCount() {
        return PublicationCountResponse.builder()
                .publishedCount(publicationRepository.countByPostStatus(GroupPostStatus.PostStatus.PUBLISHED))
                .notSuccessfulCount(publicationRepository.countByPostStatus(GroupPostStatus.PostStatus.NOT_SUCCESSFUL))
                .waitingOrProcessingCount(publicationRepository.countByPostStatus(GroupPostStatus.PostStatus.AWAITING) +
                        publicationRepository.countByPostStatus(GroupPostStatus.PostStatus.PROCESSING))
                .fbPublicationCount(publicationRepository.countByPostStatusAndSocialNetwork(GroupPostStatus.PostStatus.PUBLISHED, Group.SocialNetwork.FACEBOOK))
                .vkPublicationCount(publicationRepository.countByPostStatusAndSocialNetwork(GroupPostStatus.PostStatus.PUBLISHED, Group.SocialNetwork.VK))
                .build();
    }

    @Scheduled(fixedDelay = 60000)
    public void startPostDeffered() throws IOException {
        Page<Publication> notPublishing = publicationRepository.findNotPublishingDeferred(PageRequest.of(0, 100));
        sendPublicationToRabbit(notPublishing);
    }

    @Scheduled(fixedDelay = 30000)
    public void startPostInRealTime() throws IOException {
        Page<Publication> notPublishing = publicationRepository.findNotPublishingInRealTime(PageRequest.of(0, 100));
        sendPublicationToRabbit(notPublishing);
    }

    private void sendPublicationToRabbit(Page<Publication> notPublishing) throws IOException {
        List<Publication> publicationResponseList = notPublishing.getContent();
        log.info("batch size publiction -> {}", publicationResponseList.size());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        for (Publication response : publicationResponseList) {
            PublicationRequest request = new PublicationRequest();
            request.setText(response.getText());
            List<String> groupIds = new ArrayList<>();
            log.info("prev publication response -> {}", response);
            Set<GroupPostStatus> groupPostStatuses = response.getPostStatus();
            for (GroupPostStatus status : groupPostStatuses) {
                groupIds.add(status.getGroupId().toString());
            }
            request.setGroupIds(groupIds.toArray(String[]::new));
            request.setUserId(response.getUserId().toString());
            request.setType(response.getPostType());
            request.setPublicationId(response.getPublicationId().toString());
            request.setPostStatus(GroupPostStatus.PostStatus.PROCESSING);
            log.info("Sending message...");
            PublicationResponse resp = this.save(request);
            log.info("publication resp add queue -> {}", resp);
            String serialize = objectMapper.writeValueAsString(resp);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, serialize);
        }
    }
}
