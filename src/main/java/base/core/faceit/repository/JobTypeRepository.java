package base.core.faceit.repository;

import base.core.faceit.model.JobType;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobTypeRepository extends JpaRepository<JobType, Long> {

    Set<JobType> findByTitleIn(Iterable<String> names);

}
