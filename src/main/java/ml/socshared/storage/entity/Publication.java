package ml.socshared.storage.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ml.socshared.storage.entity.base.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "publications")
@EqualsAndHashCode(callSuper = false)
public class Publication extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "publication_id")
    private UUID publicationId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "text", length = 15000, nullable = false)
    private String text;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "publications_groups",
            joinColumns = @JoinColumn(name = "publication_id", referencedColumnName = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "group_id"))
    private Set<Group> groups;

    @Column(name = "publication_date_time")
    private Date publicationDateTime;

    @Column(name = "post_type")
    @Enumerated(EnumType.STRING)
    private PostType postType;

    @JsonManagedReference
    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL)
    private Set<GroupPostStatus> postStatus;

    public enum PostType {
        @JsonProperty("in_real_time")
        IN_REAL_TIME,
        @JsonProperty("deferred")
        DEFERRED
    }
}
