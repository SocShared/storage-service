package ml.socshared.storage.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ml.socshared.storage.domain.model.GroupModel;
import ml.socshared.storage.entity.Group;

import javax.persistence.Entity;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupResponse {

    private String groupId;
    private Group.SocialNetwork socialNetwork;
    private String name;
    private String vkId;
    private String facebookId;
    private String userId;

    public GroupResponse() {}

    public GroupResponse(Group group) {
        this.groupId = group.getGroupId().toString();
        this.socialNetwork = group.getSocialNetwork();
        this.name = group.getName();
        this.vkId = group.getGroupVkId();
        this.facebookId = group.getGroupFacebookId();
        this.userId = group.getUserId().toString();
    }

}
