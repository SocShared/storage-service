package ml.socshared.storage.entity.key;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ml.socshared.storage.entity.Group;
import ml.socshared.storage.entity.Publication;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
public class GroupPublicationPK implements Serializable {

    private UUID groupId;
    private UUID publicationId;

}
