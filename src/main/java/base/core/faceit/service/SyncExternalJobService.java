package base.core.faceit.service;

import base.core.faceit.mapper.Dto2ModelMapper;
import base.core.faceit.model.JobVacancy;
import base.core.faceit.model.dto.api.DataApiResponseDto;
import base.core.faceit.model.dto.api.JobApiResponseDto;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SyncExternalJobService {
    private static final int NUMBERS_OF_PAGES = 5;
    private static final String API_URL = "https://www.arbeitnow.com/api/job-board-api";
    private final Dto2ModelMapper<JobApiResponseDto, JobVacancy> dtoJobApiToModelMapper;
    private final JobVacancyService jobVacancyService;
    private final HttpClient httpClient;

    @PostConstruct
    @Scheduled(cron = "25 * * * * ?")
    public void syncExternalJobVacancy() {
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
            dataApiResponseDto = httpClient.get(dataApiResponseDto.getLinks().getNext(),
                            DataApiResponseDto.class);

            jobVacancies = dataApiResponseDto.getData()
                    .stream()
                    .map(dtoJobApiToModelMapper::toModel)
                    .collect(Collectors.toList());
            jobVacancyService.saveAll(filterNewJobVacancy(jobVacancies, allInternalJobSlugs));
        }
    }

    private List<JobVacancy> filterNewJobVacancy(
            List<JobVacancy> external, Set<String> internalJobsSlug) {
        return external.stream()
                .filter(e -> !internalJobsSlug.contains(e.getSlug()))
                .collect(Collectors.toList());
    }
}
