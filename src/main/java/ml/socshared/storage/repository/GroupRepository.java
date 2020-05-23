package ml.socshared.storage.repository;

import ml.socshared.storage.domain.model.GroupModel;
import ml.socshared.storage.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {

    Page<GroupModel> findByUserId(UUID userId, Pageable pageable);
    Page<GroupModel> findByUserIdAndSocialNetwork(UUID userId, Group.SocialNetwork social, Pageable pageable);
    Optional<Group> findByUserIdAndVkId(UUID userId, String vkId);
    Optional<Group> findByUserIdAndFacebookId(UUID userId, String facebookId);

}
