package base.core.faceit.repository;

import base.core.faceit.model.JobVacancy;
import base.core.faceit.model.Statistic;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JobVacancyRepository extends JpaRepository<JobVacancy, Long> {
    @Query("SELECT j FROM JobVacancy j JOIN FETCH j.jobTags JOIN FETCH "
            + "j.jobTypes JOIN FETCH j.company JOIN FETCH j.location")
    List<JobVacancy> findAllFull(Pageable pageable);

    @Query("SELECT new base.core.faceit.model.Statistic(j.location.name, "
            + "COUNT(j.location) )  FROM JobVacancy AS j  GROUP BY j.location.name")
    List<Statistic> getStatisticByLocation();

    @Query("SELECT j.slug FROM JobVacancy AS j")
    Set<String> findAllSlug();
}
