package base.core.faceit.service;

import base.core.faceit.mapper.Dto2ModelMapper;
import base.core.faceit.model.JobVacancy;
import base.core.faceit.model.dto.api.DataApiResponseDto;
import base.core.faceit.model.dto.api.JobApiResponseDto;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SyncExternalJobService implements Callable<Boolean> {
    private static final int NUMBERS_OF_PAGES = 5;
    private static final String API_URL = "https://www.arbeitnow.com/api/job-board-api";
    private final Dto2ModelMapper<JobApiResponseDto, JobVacancy> dtoJobApiToModelMapper;
    private final JobVacancyService jobVacancyService;
    private final HttpClient httpClient;

    @Override
    public Boolean call() throws Exception {
        String currentUrl = API_URL;
        List<JobVacancy> jobVacancies;
        DataApiResponseDto dataApiResponseDto = null;
        int pageCounter = NUMBERS_OF_PAGES;

        while ((pageCounter-- > 0) && (dataApiResponseDto == null
                || (dataApiResponseDto.getLinks().getNext() != null))) {

            dataApiResponseDto = httpClient.get(currentUrl,
                            DataApiResponseDto.class);
            currentUrl = dataApiResponseDto.getLinks().getNext();
            jobVacancies = dataApiResponseDto.getData()
                    .stream()
                    .map(dtoJobApiToModelMapper::toModel)
                    .collect(Collectors.toList());
            long numberOfExistingJobs = jobVacancyService.countJobVacanciesBySlugIn(jobVacancies
                    .stream()
                    .map(JobVacancy::getSlug)
                    .collect(Collectors.toList()));
            if (numberOfExistingJobs == 100) {
                return true;
            }
            if (0 == numberOfExistingJobs) {
                jobVacancyService.saveAll(jobVacancies);
            } else {
                jobVacancyService.saveAll(filterNewJobVacancy(jobVacancies));
            }
            Thread.sleep(5000);
        }
        return true;
    }

    private List<JobVacancy> filterNewJobVacancy(List<JobVacancy> external) {
        Set<String> internalJobsSlug = jobVacancyService.findAllSlugIn(external
                .stream()
                .map(JobVacancy::getSlug)
                .collect(Collectors.toList()));
        return external.stream()
                .filter(e -> !internalJobsSlug.contains(e.getSlug()))
                .collect(Collectors.toList());
    }
}
