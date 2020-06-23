package ml.socshared.storage.repository;

import ml.socshared.storage.domain.model.PublicationModel;
import ml.socshared.storage.domain.response.PublicationResponse;
import ml.socshared.storage.entity.Group;
import ml.socshared.storage.entity.GroupPostStatus;
import ml.socshared.storage.entity.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, UUID> {

    @Query("select distinct p from Publication p, GroupPostStatus gps where p.userId = :userId and gps.group.userId = :userId and gps.group.groupId = :groupId")
    Page<Publication> findByUserIdAndGroupId(@Param("userId") UUID userId, @Param("groupId") UUID groupId, Pageable pageable);

    @Query("select distinct p from Publication p, GroupPostStatus gps " +
            "where gps.publication = p and ((p.publicationDateTime is null and gps.postStatus = 'AWAITING') or (p.postType = 'DEFERRED' and " +
            " p.publicationDateTime is not null and gps.postStatus = 'AWAITING' and p.publicationDateTime <= CURRENT_TIMESTAMP))")
    Page<Publication> findNotPublishing(Pageable pageable);

    @Query("select distinct p " +
            " from Publication p, GroupPostStatus gps " +
            "where gps.publication = p and gps.postStatus = 'PUBLISHED' and p.publicationDateTime >= :date")
    Page<Publication> findByPublishingAfter(@Param("date") Date date, Pageable pageable);

    @Query("select distinct p from Publication p, GroupPostStatus gps where gps.publication = p and gps.group.groupId = :groupId")
    Page<Publication> findByGroupId(@Param("groupId") UUID groupId, Pageable pageable);

    @Query("select distinct count(p) " +
            " from Publication p, GroupPostStatus gps " +
            "where gps.publication = p and gps.postStatus = :postStatus")
    long countByPostStatus(@Param("postStatus") GroupPostStatus.PostStatus postStatus);

    @Query("select distinct count(p) " +
            " from Publication p, GroupPostStatus gps " +
            "where gps.publication = p and gps.postStatus = :postStatus and gps.socialNetwork = :socialNetwork")
    long countByPostStatusAndSocialNetwork(@Param("postStatus") GroupPostStatus.PostStatus postStatus, @Param("socialNetwork") Group.SocialNetwork socialNetwork);

}
