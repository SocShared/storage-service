package ml.socshared.storage.repository;

import ml.socshared.storage.entity.GroupPostStatus;
import ml.socshared.storage.entity.key.GroupPublicationPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GroupPostStatusRepository  extends JpaRepository<GroupPostStatus, GroupPublicationPK> {
}
