package ml.socshared.storage.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import ml.socshared.storage.entity.GroupPostStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface PublicationModel {

    UUID getPublicationId();
    UUID getUserId();
    String getText();
    LocalDateTime getPublicationDateTime();
    LocalDateTime getCreatedAt();

}
