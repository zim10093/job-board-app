package base.core.faceit.model;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "job_vacancies")
public class JobVacancy {
    @Id
    private String slug;
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
    private String title;
    @Column(columnDefinition = "VARCHAR(102400)")
    private String description;
    private boolean remote;
    private String url;
    @ManyToMany
    private Set<JobTag> jobTags;
    @ManyToMany
    private Set<JobType> jobTypes;
    @ManyToOne(fetch = FetchType.LAZY)
    private Location location;
    private LocalDateTime createdAt;
    private Long views;
}
