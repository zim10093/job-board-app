package base.core.faceit.mapper.api;

import base.core.faceit.mapper.Dto2ModelMapper;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobApiResponseDto2ModelMapper implements
        Dto2ModelMapper<JobApiResponseDto, JobVacancy> {
    private final CompanyService companyService;
    private final JobTagService jobTagService;
    private final JobTypeService jobTypeService;
    private final LocationService locationService;

    @Override
    public JobVacancy toModel(JobApiResponseDto dto) {
        JobVacancy jobVacancy = new JobVacancy();
        jobVacancy.setSlug(dto.getSlug());
        jobVacancy.setTitle(dto.getTitle());
        jobVacancy.setDescription(dto.getDescription());
        jobVacancy.setRemote(dto.isRemote());
        jobVacancy.setUrl(dto.getUrl());
        jobVacancy.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(dto.getCreatedAt()),
                TimeZone.getDefault().toZoneId()));
        jobVacancy.setCompany(verifyCompany(dto.getCompanyName()));
        jobVacancy.setJobTags(verifyJobTags(dto.getTags()));
        jobVacancy.setJobTypes(verifyJobTypes(dto.getJobTypes()));
        jobVacancy.setLocation(verifyLocation(dto.getLocation()));
        return jobVacancy;
    }

    private Company verifyCompany(String name) {
        Company company = companyService.findByName(name);
        if (company == null) {
            company = new Company();
            company.setName(name);
            companyService.save(company);
        }
        return company;
    }

    private Set<JobTag> verifyJobTags(Set<String> tags) {
        Set<JobTag> internalJobTags = jobTagService.finByTitleIn(tags);
        Set<String> internalJobTagTitles = internalJobTags.stream()
                .map(JobTag::getTitle)
                .collect(Collectors.toSet());
        Set<JobTag> newTags = tags.stream()
                .filter(e -> !internalJobTagTitles.contains(e))
                .map(e -> {
                    JobTag jobTag = new JobTag();
                    jobTag.setTitle(e);
                    return jobTag;
                })
                .collect(Collectors.toSet());
        newTags.addAll(internalJobTags);
        jobTagService.saveAll(newTags);
        return newTags;
    }

    private Set<JobType> verifyJobTypes(Set<String> types) {
        Set<JobType> internalJobTypes = jobTypeService.findByTitleIn(types);
        Set<String> internalJobTypeTitles = internalJobTypes.stream()
                .map(JobType::getTitle)
                .collect(Collectors.toSet());
        Set<JobType> newTypes = types.stream()
                .filter(e -> !internalJobTypeTitles.contains(e))
                .map(e -> {
                    JobType jobType = new JobType();
                    jobType.setTitle(e);
                    return jobType;
                })
                .collect(Collectors.toSet());
        newTypes.addAll(internalJobTypes);
        jobTypeService.saveAll(newTypes);
        return newTypes;
    }

    private Location verifyLocation(String name) {
        Location location = locationService.findByName(name);
        if (location == null) {
            location = new Location();
            location.setName(name);
            locationService.save(location);
        }
        return location;
    }
}
