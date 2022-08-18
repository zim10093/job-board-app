package base.core.faceit.mapper.response;

import base.core.faceit.mapper.Model2DtoMapper;
import base.core.faceit.model.JobTag;
import base.core.faceit.model.JobType;
import base.core.faceit.model.JobVacancy;
import base.core.faceit.model.dto.response.JobVacancyResponseDto;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class JobVacancyModel2DtoMapper implements
        Model2DtoMapper<JobVacancyResponseDto, JobVacancy> {

    @Override
    public JobVacancyResponseDto toDto(JobVacancy model) {
        JobVacancyResponseDto dto = new JobVacancyResponseDto();
        dto.setSlug(model.getSlug());
        dto.setCompanyName(model.getCompany().getName());
        dto.setTitle(model.getTitle());
        dto.setDescription(model.getDescription());
        dto.setRemote(model.isRemote());
        dto.setUrl(model.getUrl());
        dto.setJobTags(model.getJobTags()
                .stream()
                .map(JobTag::getTitle)
                .collect(Collectors.toList()));
        dto.setJobTypes(model.getJobTypes()
                .stream()
                .map(JobType::getTitle)
                .collect(Collectors.toList()));
        dto.setLocation(model.getLocation().getName());
        dto.setCreatedAt(getMsFromDateTime(model.getCreatedAt()));
        return dto;
    }

    private Long getMsFromDateTime(LocalDateTime ldt) {
        return ZonedDateTime.of(ldt, ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
