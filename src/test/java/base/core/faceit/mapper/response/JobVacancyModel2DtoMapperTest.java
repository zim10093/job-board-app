package base.core.faceit.mapper.response;

import base.core.faceit.model.Company;
import base.core.faceit.model.JobTag;
import base.core.faceit.model.JobType;
import base.core.faceit.model.JobVacancy;
import base.core.faceit.model.Location;
import base.core.faceit.model.dto.response.JobVacancyResponseDto;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TimeZone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JobVacancyModel2DtoMapperTest {
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
    private static JobVacancyModel2DtoMapper jobVacancyModel2DtoMapper;
    private JobVacancy jobVacancy;

    @BeforeAll
    static void beforeAll() {
        jobVacancyModel2DtoMapper = new JobVacancyModel2DtoMapper();
    }

    @BeforeEach
    void setUp() {
        jobVacancy = new JobVacancy();
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
    }

    @Test
    public void toDto_ok() {
        JobVacancyResponseDto jobVacancyResponseDto = jobVacancyModel2DtoMapper.toDto(jobVacancy);
        Assertions.assertNotNull(jobVacancyResponseDto);
        Assertions.assertEquals(SLUG, jobVacancyResponseDto.getSlug());
        Assertions.assertEquals(COMPANY_NAME, jobVacancyResponseDto.getCompanyName());
        Assertions.assertEquals(TITLE, jobVacancyResponseDto.getTitle());
        Assertions.assertEquals(DESCRIPTION, jobVacancyResponseDto.getDescription());
        Assertions.assertEquals(REMOTE, jobVacancyResponseDto.isRemote());
        Assertions.assertEquals(URL, jobVacancyResponseDto.getUrl());
        Assertions.assertEquals(TAGS, String.join(",", jobVacancyResponseDto.getJobTags()));
        Assertions.assertEquals(JOB_TYPES, String.join(",",
                jobVacancyResponseDto.getJobTypes()));
        Assertions.assertEquals(LOCATION, jobVacancyResponseDto.getLocation());
        Assertions.assertEquals(CREATED_AT, jobVacancyResponseDto.getCreatedAt());
    }
}