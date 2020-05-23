package ml.socshared.storage.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import ml.socshared.storage.entity.Group;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface GroupModel {

    UUID getGroupId();
    UUID getUserId();
    String getName();
    Group.SocialNetwork getSocialNetwork();
    String getFacebookId();
    String getVkId();

}
