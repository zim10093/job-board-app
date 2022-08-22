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
    private static final int SLEEP_TIME_MS = 10000;
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

        while ((pageCounter-- > 0) && (dataApiResponseDto == null || (currentUrl != null))) {
            dataApiResponseDto = httpClient.get(currentUrl, DataApiResponseDto.class);
            currentUrl = dataApiResponseDto.getLinks().getNext();
            List<String> externalSlugs = dataApiResponseDto.getData()
                    .stream()
                    .map(JobApiResponseDto::getSlug)
                    .collect(Collectors.toList());
            Set<String> internalSlugs = jobVacancyService.findAllSlugIn(externalSlugs);
            externalSlugs.removeAll(internalSlugs);

            if (externalSlugs.isEmpty()) {
                return false;
            }

            jobVacancies = dataApiResponseDto.getData()
                    .stream()
                    .filter(e -> externalSlugs.contains(e.getSlug()))
                    .map(dtoJobApiToModelMapper::toModel)
                    .collect(Collectors.toList());
            jobVacancyService.saveAll(jobVacancies);
            Thread.sleep(SLEEP_TIME_MS);
        }
        return true;
    }
}
