package ml.socshared.storage.repository;

import ml.socshared.storage.domain.model.PublicationModel;
import ml.socshared.storage.entity.Group;
import ml.socshared.storage.entity.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, UUID> {

    @Query("select distinct p from Publication p join Group g where p.userId = :userId and g.userId = :userId and g.groupId = :groupId")
    Page<PublicationModel> findByUserIdAndGroupId(@Param("userId") UUID userId, @Param("groupId") UUID groupId, Pageable pageable);

    @Query("select distinct p from Publication p, GroupPostStatus gps " +
            "where gps.publication = p and ((p.publicationDateTime is null and gps.postStatus = 'AWAITING') or (p.postType = 'DEFERRED' and " +
            " p.publicationDateTime is not null and gps.postStatus = 'AWAITING' and p.publicationDateTime <= CURRENT_TIMESTAMP))")
    Page<PublicationModel> findNotPublishing(Pageable pageable);

    @Query("select distinct p from Publication p where p.publicationDateTime >= :date")
    Page<PublicationModel> findPublishingAfter(@Param("date") Date date, Pageable pageable);

    Page<PublicationModel> findByGroups(Group g, Pageable pageable);

}
