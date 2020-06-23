package ml.socshared.storage.service;

import ml.socshared.storage.domain.model.GroupModel;
import ml.socshared.storage.domain.request.GroupRequest;
import ml.socshared.storage.domain.response.GroupCountResponse;
import ml.socshared.storage.domain.response.GroupResponse;
import ml.socshared.storage.entity.Group;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface GroupService {

    GroupResponse save(GroupRequest request);
    void deleteById(UUID groupId);
    void deleteByVkId(UUID userId, String vkId);
    void deleteByFbId(UUID userId, String fbId);
    GroupResponse findById(UUID groupId);
    Page<GroupModel> findByUserId(UUID userId, Integer page, Integer size);
    Page<GroupModel> findByUserIdAndSocialNetwork(UUID userId, Group.SocialNetwork socialNetwork, Integer page, Integer size);
    GroupResponse findByUserIdAndVkId(UUID userId, String vkId);
    GroupResponse findByUserIdAndFacebookId(UUID userId, String facebookId);
    void deleteVkGroupsByUserId(UUID userId);
    void deleteFacebookGroupsByUserId(UUID userId);
    GroupCountResponse groupCount();

}
