package ml.socshared.storage.repository;

import ml.socshared.storage.domain.model.PublicationModel;
import ml.socshared.storage.entity.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, UUID> {

    @Query("select p from Publication p join Group g where p.userId = :userId and g.userId = :userId and g.groupId = :groupId")
    Page<PublicationModel> findByUserIdAndGroupId(@Param("userId") UUID userId, @Param("groupId") UUID groupId, Pageable pageable);

    @Query("select p from Publication p join GroupPostStatus gps " +
            "where (p.publicationDateTime is null and gps.postStatus = 'AWAITING') or " +
            "(p.publicationDateTime is not null and p.publicationDateTime < CURRENT_TIME and gps.postStatus = 'AWAITING')")
    Page<PublicationModel> findNotPublishing(Pageable pageable);

}
