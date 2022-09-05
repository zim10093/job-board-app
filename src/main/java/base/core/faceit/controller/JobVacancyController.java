package base.core.faceit.controller;

import base.core.faceit.mapper.Model2DtoMapper;
import base.core.faceit.model.JobVacancy;
import base.core.faceit.model.Statistic;
import base.core.faceit.model.dto.response.JobVacancyResponseDto;
import base.core.faceit.model.dto.response.JobVacancyShortResponseDto;
import base.core.faceit.service.JobVacancyService;
import base.core.faceit.util.SortUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobVacancyController {
    private final Model2DtoMapper<JobVacancyShortResponseDto, JobVacancy> modelToDtoShortMapper;
    private final Model2DtoMapper<JobVacancyResponseDto, JobVacancy> modelToDtoMapper;
    private final JobVacancyService jobVacancyService;
    private final SortUtil sortUtil;

    @GetMapping
    public List<JobVacancyShortResponseDto> getAll(
            @RequestParam (defaultValue = "20") Integer count,
            @RequestParam (defaultValue = "0") Integer page,
            @RequestParam (defaultValue = "createdAt") String sortBy) {
        Sort sort = Sort.by(sortUtil.getOrders(sortBy));
        PageRequest pageRequest = PageRequest.of(page, count, sort);
        return jobVacancyService.findAll(pageRequest)
                .stream()
                .map(modelToDtoShortMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{slug}")
    public JobVacancyResponseDto getBySlug(@PathVariable String slug) {
        jobVacancyService.incrementViewsBySlug(slug);
        return modelToDtoMapper.toDto(jobVacancyService.findBySlug(slug).get());
    }

    @GetMapping("/statistic")
    public List<Statistic> getStatistic() {
        return jobVacancyService.getStatisticByLocation();
    }

    @GetMapping("/top{limit}")
    public List<JobVacancyShortResponseDto> getTop(@PathVariable int limit) {
        return jobVacancyService.findTopByViews(limit)
                .stream()
                .map(modelToDtoShortMapper::toDto)
                .collect(Collectors.toList());
    }
}
