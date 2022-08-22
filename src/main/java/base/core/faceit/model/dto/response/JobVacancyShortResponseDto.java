package base.core.faceit.model.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobVacancyShortResponseDto {
    private String slug;
    private String companyName;
    private String title;
    private boolean remote;
    private String url;
    private List<String> jobTags;
    private List<String> jobTypes;
    private String location;
    private long createdAt;
    private long views;
}
