package base.core.faceit.controller;

import base.core.faceit.model.Company;
import base.core.faceit.model.JobTag;
import base.core.faceit.model.JobType;
import base.core.faceit.model.JobVacancy;
import base.core.faceit.model.Location;
import base.core.faceit.model.Statistic;
import base.core.faceit.service.JobVacancyService;
import base.core.faceit.util.Scheduler;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = "test")
@SpringBootTest
@AutoConfigureMockMvc
class JobVacancyControllerTest {
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
    //disable sync job for this test
    @MockBean
    private Scheduler scheduler;

    @MockBean
    private JobVacancyService jobVacancyService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    public void getStatistic_ok() {
        List<Statistic> mockResponse = List.of(new Statistic("Berlin", 5L),
                new Statistic("Praga", 2L), new Statistic("Kyiv", 3L));
        Mockito.when(jobVacancyService.getStatisticByLocation()).thenReturn(mockResponse);

        RestAssuredMockMvc.when()
                .get("jobs/statistic")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(3))
                .body("[0].count", Matchers.equalTo(5))
                .body("[0].location", Matchers.equalTo("Berlin"))
                .body("[1].count", Matchers.equalTo(2))
                .body("[1].location", Matchers.equalTo("Praga"))
                .body("[2].count", Matchers.equalTo(3))
                .body("[2].location", Matchers.equalTo("Kyiv"));
    }

    @Test
    public void getTopTen_ok() {
        Mockito.when(jobVacancyService.findTopByCreatedAt())
                .thenReturn(createListOfJobVacancies(10));

        RestAssuredMockMvc.when()
                .get("jobs/top10")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(10))
                .body("[0].slug", Matchers.equalTo("some slug : 0"))
                .body("[9].slug", Matchers.equalTo("some slug : 9"));
    }

    @Test
    public void getAllOnlyPagination_ok() {
        Mockito.when(jobVacancyService.findAll(Mockito.any()))
                .thenReturn(createListOfJobVacancies(5));

        RestAssuredMockMvc.when()
                .get("jobs?count=5")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(5));
    }

    @Test
    public void getAllPaginationAndSort_ok() {
        Mockito.when(jobVacancyService.findAll(Mockito.any()))
                .thenReturn(createListOfJobVacancies(5));

        RestAssuredMockMvc.when()
                .get("jobs?count=5&page0&sortBy=slug:DESC;location:ASC")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(5));
    }

    @Test
    public void getAllOnlySort_ok() {
        Mockito.when(jobVacancyService.findAll(Mockito.any()))
                .thenReturn(createListOfJobVacancies(5));

        RestAssuredMockMvc.when()
                .get("jobs?sortBy=slug:DESC;location")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(5));
    }

    private List<JobVacancy> createListOfJobVacancies(int size) {
        return IntStream.range(0, size)
                .mapToObj(e -> createJobVacancyWithSlug(SLUG + " : " + e))
                .collect(Collectors.toList());
    }

    private JobVacancy createJobVacancyWithSlug(String slug) {
        JobVacancy jobVacancy = new JobVacancy();
        jobVacancy.setSlug(slug);
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
