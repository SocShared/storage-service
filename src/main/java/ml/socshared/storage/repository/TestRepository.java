package ml.socshared.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ml.socshared.storage.domain.TestObject;

@Repository
public interface TestRepository extends JpaRepository<TestObject, Integer> {
}
