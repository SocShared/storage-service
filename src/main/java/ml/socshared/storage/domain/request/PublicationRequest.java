package ml.socshared.storage.domain.request;

import lombok.Getter;
import lombok.Setter;
import ml.socshared.storage.entity.Group;
import ml.socshared.storage.entity.GroupPostStatus;
import ml.socshared.storage.entity.Publication;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class PublicationRequest {

    private String publicationId;
    private GroupPostStatus.PostStatus postStatus;
    private Date publicationDateTime;
    @NotEmpty
    private String userId;
    @NotEmpty
    private String[] groupIds;
    @NotNull
    private Publication.PostType type;
    @NotNull
    private String text;

}
