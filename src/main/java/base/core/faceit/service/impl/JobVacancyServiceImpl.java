package base.core.faceit.service.impl;

import base.core.faceit.model.JobVacancy;
import base.core.faceit.model.Statistic;
import base.core.faceit.repository.JobVacancyRepository;
import base.core.faceit.service.JobVacancyService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobVacancyServiceImpl implements JobVacancyService {
    private final JobVacancyRepository jobVacancyRepository;

    @Override
    public List<JobVacancy> saveAll(List<JobVacancy> jobVacancies) {
        return jobVacancyRepository.saveAll(jobVacancies);
    }

    @Override
    public List<JobVacancy> findAll(Pageable pageable) {
        return jobVacancyRepository.findAllFull(pageable);
    }

    @Override
    public List<Statistic> getStatisticByLocation() {
        return jobVacancyRepository.getStatisticByLocation();
    }

    @Override
    public Set<String> findAllSlugIn(Iterable<String> slugs) {
        return jobVacancyRepository.findAllSlugIn(slugs);
    }

    @Override
    public List<JobVacancy> findTopByCreatedAt() {
        return jobVacancyRepository.findTopByCreatedAt();
    }

    @Override
    public Long countJobVacanciesBySlugIn(Iterable<String> slugs) {
        return jobVacancyRepository.countJobVacanciesBySlugIn(slugs);
    }
}
