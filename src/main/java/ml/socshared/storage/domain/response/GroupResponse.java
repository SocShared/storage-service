package ml.socshared.storage.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ml.socshared.storage.entity.Group;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupResponse {

    private String groupId;
    private Group.SocialNetwork socialNetwork;
    private String name;
    private String groupVkId;
    private String groupFacebookId;
    private String userId;

    public GroupResponse() {}

    public GroupResponse(Group group) {
        this.groupId = group.getGroupId().toString();
        this.socialNetwork = group.getSocialNetwork();
        this.name = group.getName();
        this.groupVkId = group.getGroupVkId();
        this.groupFacebookId = group.getGroupFacebookId();
        this.userId = group.getUserId().toString();
    }

}
