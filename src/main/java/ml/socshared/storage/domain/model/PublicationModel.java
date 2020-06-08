package ml.socshared.storage.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import ml.socshared.storage.entity.GroupPostStatus;
import ml.socshared.storage.entity.Publication;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublicationModel {

    UUID publicationId;
    UUID userId;
    String text;
    Date publicationDateTime;
    LocalDateTime createdAt;
    Publication.PostType postType;
    UUID groupId;
    GroupPostStatus.PostStatus postStatus;

}
