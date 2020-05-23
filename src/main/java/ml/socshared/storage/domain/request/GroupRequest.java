package ml.socshared.storage.domain.request;

import lombok.Getter;
import lombok.Setter;
import ml.socshared.storage.entity.Group;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GroupRequest {

    @NotEmpty
    private String userId;
    @NotEmpty
    private String vkId;
    @NotEmpty
    private String fbId;
    @NotNull
    private String name;
    @NotNull
    private Group.SocialNetwork socialNetwork;

}
