package ml.socshared.storage.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import ml.socshared.storage.entity.GroupPostStatus;
import ml.socshared.storage.entity.Publication;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface PublicationModel {

    UUID getPublicationId();
    UUID getUserId();
    String getText();
    Date getPublicationDateTime();
    LocalDateTime getCreatedAt();
    Publication.PostType getPostType();
    UUID getGroupId();
    GroupPostStatus.PostStatus getPostStatus();

}
