package ml.socshared.storage.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ml.socshared.storage.entity.GroupPostStatus;
import ml.socshared.storage.entity.Publication;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublicationResponse {

    private UUID publicationId;
    private UUID userId;
    private String text;
    private Date publicationDateTime;
    private LocalDateTime createdAt;
    private Publication.PostType postType;
    private Set<GroupPostStatus> postStatus;

    public PublicationResponse() {}

    public PublicationResponse(Publication publication) {
        this.publicationId = publication.getPublicationId();
        this.userId = publication.getUserId();
        this.text = publication.getText();
        this.publicationDateTime = publication.getPublicationDateTime();
        this.postType = publication.getPostType();
        this.createdAt = publication.getCreatedAt();
        this.postStatus = publication.getPostStatus();
    }

}
