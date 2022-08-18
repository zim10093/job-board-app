package base.core.faceit.service;

import base.core.faceit.model.JobType;
import java.util.List;
import java.util.Set;

public interface JobTypeService {

    Set<JobType> findByTitleIn(Iterable<String> names);

    List<JobType> saveAll(Iterable<JobType> jobTypes);
}
