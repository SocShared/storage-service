package ml.socshared.storage.repository;

import ml.socshared.storage.domain.model.GroupModel;
import ml.socshared.storage.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {

    Page<GroupModel> findByUserId(UUID userId, Pageable pageable);
    Page<GroupModel> findByUserIdAndSocialNetwork(UUID userId, Group.SocialNetwork social, Pageable pageable);
    Optional<Group> findDistinctTopByUserIdAndGroupVkId(UUID userId, String vkId);
    Optional<Group> findDistinctTopByUserIdAndGroupFacebookId(UUID userId, String facebookId);

    @Transactional
    @Modifying
    void deleteByUserIdAndSocialNetwork(UUID userId, Group.SocialNetwork socialNetwork);

    long countBySocialNetwork(Group.SocialNetwork socialNetwork);

}
