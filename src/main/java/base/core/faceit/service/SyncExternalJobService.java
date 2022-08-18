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
    private static final int NUMBERS_OF_PAGES = 10;
    private static final String API_URL = "https://www.arbeitnow.com/api/job-board-api";
    private final Dto2ModelMapper<JobApiResponseDto, JobVacancy> dtoJobApiToModelMapper;
    private final JobVacancyService jobVacancyService;
    private final HttpClient httpClient;

    @Override
    public Boolean call() throws Exception {
        DataApiResponseDto dataApiResponseDto = httpClient
                .get(API_URL, DataApiResponseDto.class);
        Set<String> allInternalJobSlugs = jobVacancyService.findAllSlug();
        List<JobVacancy> jobVacancies = dataApiResponseDto.getData()
                .stream()
                .map(dtoJobApiToModelMapper::toModel)
                .collect(Collectors.toList());

        jobVacancyService.saveAll(filterNewJobVacancy(jobVacancies, allInternalJobSlugs));
        int pageCounter = NUMBERS_OF_PAGES;

        while ((--pageCounter > 0) && (dataApiResponseDto.getLinks().getNext() != null)) {
            Thread.sleep(5000);
            dataApiResponseDto = httpClient.get(dataApiResponseDto.getLinks().getNext(),
                            DataApiResponseDto.class);

            jobVacancies = dataApiResponseDto.getData()
                    .stream()
                    .map(dtoJobApiToModelMapper::toModel)
                    .collect(Collectors.toList());
            jobVacancyService.saveAll(filterNewJobVacancy(jobVacancies, allInternalJobSlugs));
        }
        return true;
    }

    private List<JobVacancy> filterNewJobVacancy(
            List<JobVacancy> external, Set<String> internalJobsSlug) {
        return external.stream()
                .filter(e -> !internalJobsSlug.contains(e.getSlug()))
                .collect(Collectors.toList());
    }
}
