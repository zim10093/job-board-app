package base.core.faceit.model.dto.api;

import java.util.List;
import lombok.Getter;

@Getter
public class DataApiResponseDto {
    private List<JobApiResponseDto> data;
    private LinksApiResponseDto links;
}
