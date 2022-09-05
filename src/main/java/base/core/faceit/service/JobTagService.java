package base.core.faceit.service;

import base.core.faceit.model.JobTag;
import java.util.List;
import java.util.Set;

public interface JobTagService {
    Set<JobTag> finByTitleIn(Iterable<String> names);

    List<JobTag> saveAll(Iterable<JobTag> tags);
}
