package base.core.faceit.model.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import lombok.Getter;

@Getter
public class JobApiResponseDto {
    private String slug;
    @JsonProperty("company_name")
    private String companyName;
    private String title;
    private String description;
    private boolean remote;
    private String url;
    private Set<String> tags;
    @JsonProperty("job_types")
    private Set<String> jobTypes;
    private String location;
    @JsonProperty("created_at")
    private long createdAt;
}
