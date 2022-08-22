package base.core.faceit.repository;

import base.core.faceit.model.JobVacancy;
import base.core.faceit.model.Statistic;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface JobVacancyRepository extends JpaRepository<JobVacancy, Long> {
    @Query("SELECT j FROM JobVacancy j JOIN FETCH j.jobTags JOIN FETCH "
            + "j.jobTypes JOIN FETCH j.company JOIN FETCH j.location")
    List<JobVacancy> findAllFull(Pageable pageable);

    @Query("SELECT new base.core.faceit.model.Statistic(j.location.name, "
            + "COUNT(j.location) )  FROM JobVacancy AS j  GROUP BY j.location.name")
    List<Statistic> getStatisticByLocation();

    @Query("SELECT j.slug FROM JobVacancy AS j WHERE j.slug IN :slugs")
    Set<String> findAllSlugIn(@Param("slugs") Iterable<String> slugs);

    @Query(value = "SELECT * FROM job_vacancies ORDER BY views DESC , created_at DESC LIMIT 10;",
            nativeQuery = true)
    List<JobVacancy> findTopByCreatedAt();

    Optional<JobVacancy> findBySlug(String slug);

    @Modifying
    @Transactional
    @Query(value = "UPDATE JobVacancy j SET j.views = j.views + 1 WHERE j.slug = :slugg")
    void incrementViewsBySlug(@Param("slugg") String slug);
}
