package base.core.faceit.model.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JobVacancyResponseDto {
    private String slug;
    private String companyName;
    private String title;
    private String description;
    private boolean remote;
    private String url;
    private List<String> jobTags;
    private List<String> jobTypes;
    private String location;
    private long createdAt;
    private long views;
}
