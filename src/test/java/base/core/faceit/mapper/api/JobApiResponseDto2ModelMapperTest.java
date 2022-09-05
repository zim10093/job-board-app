package base.core.faceit.mapper.api;

import base.core.faceit.model.Company;
import base.core.faceit.model.JobTag;
import base.core.faceit.model.JobType;
import base.core.faceit.model.JobVacancy;
import base.core.faceit.model.Location;
import base.core.faceit.model.dto.api.JobApiResponseDto;
import base.core.faceit.service.CompanyService;
import base.core.faceit.service.JobTagService;
import base.core.faceit.service.JobTypeService;
import base.core.faceit.service.LocationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JobApiResponseDto2ModelMapperTest {
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
    private static final String JOB_API_RESPONSE_JSON = "{\"slug\": \"%s\", "
        + "\"company_name\": \"%s\", \"title\": \"%s\", \"description\": \"%s\", \"remote\":%b, "
        + "\"url\": \"%s\", \"tags\": [\"%s\"], \"job_types\": [\"%s\"], \"location\": \"%s\", "
        + "\"created_at\": %d}";
    private static ObjectMapper objectMapper;
    @InjectMocks
    private JobApiResponseDto2ModelMapper jobApiResponseDto2ModelMapper;
    @Mock
    private  CompanyService companyService;
    @Mock
    private JobTagService jobTagService;
    @Mock
    private JobTypeService jobTypeService;
    @Mock
    private LocationService locationService;

    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void toModel_existingComponents_ok() throws JsonProcessingException {
        Company company = getCompany();
        Mockito.when(companyService.findByName(COMPANY_NAME)).thenReturn(Optional.of(company));

        JobTag jobTag = getJobTag();
        Mockito.when(jobTagService.finByTitleIn(Set.of(TAGS))).thenReturn(Set.of(jobTag));

        JobType jobType = getJobType();
        Mockito.when(jobTypeService.findByTitleIn(Set.of(JOB_TYPES)))
                .thenReturn(Set.of(jobType));

        Location location = getLocation();
        Mockito.when(locationService.findByName(LOCATION)).thenReturn(Optional.of(location));

        String url = String.format(JOB_API_RESPONSE_JSON, SLUG, COMPANY_NAME, TITLE, DESCRIPTION,
                REMOTE, URL, TAGS, JOB_TYPES, LOCATION, CREATED_AT);
        JobApiResponseDto jobApiResponseDto =
                objectMapper.readValue(url, JobApiResponseDto.class);
        JobVacancy jobVacancy = jobApiResponseDto2ModelMapper.toModel(jobApiResponseDto);

        Assertions.assertNotNull(jobVacancy);
        Assertions.assertEquals(SLUG, jobVacancy.getSlug());
        Assertions.assertEquals(COMPANY_NAME, jobVacancy.getCompany().getName());
        Assertions.assertEquals(TITLE, jobVacancy.getTitle());
        Assertions.assertEquals(DESCRIPTION, jobVacancy.getDescription());
        Assertions.assertFalse(jobVacancy.isRemote());
        Assertions.assertEquals(URL, jobVacancy.getUrl());
        Assertions.assertEquals(TAGS, jobVacancy.getJobTags().stream()
                .map(JobTag::getTitle).collect(Collectors.joining(",")));
        Assertions.assertEquals(JOB_TYPES, jobVacancy.getJobTypes().stream()
                .map(JobType::getTitle).collect(Collectors.joining(",")));
        Assertions.assertEquals(LOCATION, jobVacancy.getLocation().getName());
    }

    @Test
    public void toModel_newComponent_ok() throws JsonProcessingException {
        Mockito.when(companyService.findByName(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(jobTagService.finByTitleIn(Mockito.any())).thenReturn(Collections.emptySet());
        Mockito.when(jobTypeService.findByTitleIn(Mockito.any()))
                .thenReturn(Collections.emptySet());
        Mockito.when(locationService.findByName(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(companyService.save(Mockito.any())).thenReturn(getCompany());
        Mockito.when(locationService.save(Mockito.any())).thenReturn(getLocation());

        String url = String.format(JOB_API_RESPONSE_JSON, SLUG, COMPANY_NAME, TITLE, DESCRIPTION,
                REMOTE, URL, TAGS, JOB_TYPES, LOCATION, CREATED_AT);
        JobApiResponseDto jobApiResponseDto =
                objectMapper.readValue(url, JobApiResponseDto.class);
        JobVacancy jobVacancy = jobApiResponseDto2ModelMapper.toModel(jobApiResponseDto);

        Assertions.assertNotNull(jobVacancy);
        Assertions.assertEquals(SLUG, jobVacancy.getSlug());
        Assertions.assertEquals(COMPANY_NAME, jobVacancy.getCompany().getName());
        Assertions.assertEquals(TITLE, jobVacancy.getTitle());
        Assertions.assertEquals(DESCRIPTION, jobVacancy.getDescription());
        Assertions.assertFalse(jobVacancy.isRemote());
        Assertions.assertEquals(URL, jobVacancy.getUrl());
        Assertions.assertEquals(TAGS, jobVacancy.getJobTags().stream()
                .map(JobTag::getTitle).collect(Collectors.joining(",")));
        Assertions.assertEquals(JOB_TYPES, jobVacancy.getJobTypes().stream()
                .map(JobType::getTitle).collect(Collectors.joining(",")));
        Assertions.assertEquals(LOCATION, jobVacancy.getLocation().getName());
    }

    private static Company getCompany() {
        Company company = new Company();
        company.setName(COMPANY_NAME);
        company.setId(1L);
        return company;
    }

    private static JobTag getJobTag() {
        JobTag jobTag = new JobTag();
        jobTag.setTitle(TAGS);
        jobTag.setId(1L);
        return jobTag;
    }

    private static JobType getJobType() {
        JobType jobType = new JobType();
        jobType.setTitle(JOB_TYPES);
        jobType.setId(1L);
        return jobType;
    }

    private static Location getLocation() {
        Location location = new Location();
        location.setName(LOCATION);
        location.setId(1L);
        return location;
    }
}
