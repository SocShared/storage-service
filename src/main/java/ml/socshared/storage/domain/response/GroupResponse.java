package ml.socshared.storage.domain.response;

import lombok.*;
import ml.socshared.storage.domain.model.GroupModel;
import ml.socshared.storage.entity.Group;

import javax.persistence.Entity;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class GroupResponse {

    private String groupId;
    private Group.SocialNetwork socialNetwork;
    private String name;
    private String vkId;
    private String fbId;
    private String userId;

    public GroupResponse() {}

    public GroupResponse(Group group) {
        this.groupId = group.getGroupId().toString();
        this.socialNetwork = group.getSocialNetwork();
        this.name = group.getName();
        this.vkId = group.getVkId();
        this.fbId = group.getFacebookId();
        this.userId = group.getUserId().toString();
    }

}
