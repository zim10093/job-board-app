package base.core.faceit.service.impl;

import base.core.faceit.model.JobType;
import base.core.faceit.repository.JobTypeRepository;
import base.core.faceit.service.JobTypeService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobTypeServiceImpl implements JobTypeService {
    private final JobTypeRepository jobTypeRepository;

    @Override
    public Set<JobType> findByTitleIn(Iterable<String> names) {
        return jobTypeRepository.findByTitleIn(names);
    }

    @Override
    public List<JobType> saveAll(Iterable<JobType> jobTypes) {
        return jobTypeRepository.saveAll(jobTypes);
    }
}
