package base.core.faceit.repository;

import base.core.faceit.model.JobTag;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobTagRepository extends JpaRepository<JobTag, Long> {
    Set<JobTag> findByTitleIn(Iterable<String> names);

}
