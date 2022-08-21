package base.core.faceit.service;

import base.core.faceit.model.JobVacancy;
import base.core.faceit.model.Statistic;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface JobVacancyService {
    List<JobVacancy> saveAll(List<JobVacancy> jobVacancies);

    List<JobVacancy> findAll(Pageable pageable);

    List<Statistic> getStatisticByLocation();

    Set<String> findAllSlugIn(Iterable<String> slugs);

    List<JobVacancy> findTopByCreatedAt();
}
