package base.core.faceit.service;

import base.core.faceit.mapper.Dto2ModelMapper;
import base.core.faceit.model.Company;
import base.core.faceit.model.JobTag;
import base.core.faceit.model.JobType;
import base.core.faceit.model.JobVacancy;
import base.core.faceit.model.Location;
import base.core.faceit.model.dto.api.DataApiResponseDto;
import base.core.faceit.model.dto.api.JobApiResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.TimeZone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SyncExternalJobServiceTest {
    private static final String SLUG = "some slug";
    private static final String COMPANY_NAME = "some company";
    private static final String TITLE = "some title";
    private static final String DESCRIPTION = "some description";
    private static final boolean REMOTE = false;
    private static final String URL = "https://some-url.com";
    private static final String TAGS = "some tag";
    private static final String JOB_TYPES = "some job type";
    private static final String LOCATION = "some location";
    private static final long CREATED_AT = 1660856656L;
    private static final String DATA_API_RESPONSE_JSON = "{\"data\":[{\"slug\":\"some slug\","
        + "\"company_name\":\"some company\",\"title\":\"some title\","
        + "\"description\":\"some description\",\"remote\":false,\"url\":\"url.com\","
        + "\"tags\":[\"some tag\"],\"job_types\":[\"some job types\"],"
        + "\"location\":\"some location\",\"created_at\":1661102945}],"
        + "\"links\":{\"next\":null}}";

    private static ObjectMapper objectMapper;
    @InjectMocks
    private SyncExternalJobService syncExternalJobService;

    @Mock
    private Dto2ModelMapper<JobApiResponseDto, JobVacancy> dtoJobApiToModelMapper;
    @Mock
    private JobVacancyService jobVacancyService;
    @Mock
    HttpClient httpClient;

    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void call_validHttpResponseAndEmptyDb_ok() throws Exception {
        DataApiResponseDto dataApiResponseDto = objectMapper.readValue(DATA_API_RESPONSE_JSON,
                DataApiResponseDto.class);
        Mockito.when(httpClient.get(Mockito.any(), Mockito.any())).thenReturn(dataApiResponseDto);

        Mockito.when(dtoJobApiToModelMapper.toModel(Mockito.any())).thenReturn(createJobVacancy());
        Mockito.when(jobVacancyService.findAllSlugIn(Mockito.any()))
                .thenReturn(Collections.emptySet());

        long startTime = System.currentTimeMillis();
        Assertions.assertTrue(syncExternalJobService.call());
        Assertions.assertTrue((System.currentTimeMillis() - startTime) / 1000 > 9);
    }

    @Test
    public void call_validHttpResponseJobsAlreadyInDb() throws Exception {
        DataApiResponseDto dataApiResponseDto = objectMapper.readValue(DATA_API_RESPONSE_JSON,
                DataApiResponseDto.class);
        Mockito.when(httpClient.get(Mockito.any(), Mockito.any())).thenReturn(dataApiResponseDto);

        Mockito.when(jobVacancyService.findAllSlugIn(Mockito.any()))
                .thenReturn(Set.of(SLUG));

        long startTime = System.currentTimeMillis();
        Assertions.assertFalse(syncExternalJobService.call());
        Assertions.assertTrue((System.currentTimeMillis() - startTime) / 1000 < 9);
    }

    private JobVacancy createJobVacancy( ) {
        JobVacancy jobVacancy = new JobVacancy();
        jobVacancy.setSlug(SLUG);
        Company company = new Company();
        company.setId(1L);
        company.setName(COMPANY_NAME);
        jobVacancy.setCompany(company);
        jobVacancy.setTitle(TITLE);
        jobVacancy.setDescription(DESCRIPTION);
        jobVacancy.setRemote(REMOTE);
        jobVacancy.setUrl(URL);
        JobTag jobTag = new JobTag();
        jobTag.setId(1L);
        jobTag.setTitle(TAGS);
        jobVacancy.setJobTags(Set.of(jobTag));
        JobType jobType = new JobType();
        jobType.setId(1L);
        jobType.setTitle(JOB_TYPES);
        jobVacancy.setJobTypes(Set.of(jobType));
        Location location = new Location();
        location.setId(1L);
        location.setName(LOCATION);
        jobVacancy.setLocation(location);
        jobVacancy.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(CREATED_AT),
                TimeZone.getDefault().toZoneId()));
        return jobVacancy;
    }
}
