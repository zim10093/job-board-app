package base.core.faceit.controller;

import base.core.faceit.mapper.Model2DtoMapper;
import base.core.faceit.model.JobVacancy;
import base.core.faceit.model.Statistic;
import base.core.faceit.model.dto.response.JobVacancyResponseDto;
import base.core.faceit.service.JobVacancyService;
import base.core.faceit.service.SyncExternalJobService;
import base.core.faceit.util.SortUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobVacancyController {
    private final Model2DtoMapper<JobVacancyResponseDto, JobVacancy> jobVacancyToResponseDtoMapper;
    private final JobVacancyService jobVacancyService;
    private final SortUtil sortUtil;
    private final SyncExternalJobService syncExternalJobService;

    @GetMapping
    public List<JobVacancyResponseDto> getAll(
            @RequestParam (defaultValue = "20") Integer count,
            @RequestParam (defaultValue = "0") Integer page,
            @RequestParam (defaultValue = "createdAt") String sortBy) {
        Sort sort = Sort.by(sortUtil.getOrders(sortBy));
        PageRequest pageRequest = PageRequest.of(page, count, sort);
        return jobVacancyService.findAll(pageRequest)
                .stream()
                .map(jobVacancyToResponseDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/update")
    public String update() {
        syncExternalJobService.syncExternalJobVacancy();
        return "Update done";
    }

    @GetMapping("/statistic")
    public List<Statistic> test() {
        return jobVacancyService.getStatisticByLocation();
    }

    @GetMapping("/top10")
    public List<JobVacancyResponseDto> getTopTen() {
        Sort sort = Sort.by(sortUtil.getOrders("createdAt"));
        PageRequest pageRequest = PageRequest.of(0, 10, sort);
        return jobVacancyService.findAll(pageRequest)
                .stream()
                .map(jobVacancyToResponseDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
