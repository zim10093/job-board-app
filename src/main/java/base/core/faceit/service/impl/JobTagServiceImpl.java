package base.core.faceit.service.impl;

import base.core.faceit.model.JobTag;
import base.core.faceit.repository.JobTagRepository;
import base.core.faceit.service.JobTagService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobTagServiceImpl implements JobTagService {
    private final JobTagRepository jobTagRepository;

    @Override
    public Set<JobTag> finByTitleIn(Iterable<String> names) {
        return jobTagRepository.findByTitleIn(names);
    }

    @Override
    public List<JobTag> saveAll(Iterable<JobTag> tags) {
        return jobTagRepository.saveAll(tags);
    }
}
