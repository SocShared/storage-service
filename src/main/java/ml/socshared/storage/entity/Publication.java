package ml.socshared.storage.entity;

import lombok.Getter;
import lombok.Setter;
import ml.socshared.storage.entity.base.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "publications")
public class Publication extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID publicationId;

    @Column(name = "text")
    private String text;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "publications_groups",
            joinColumns = @JoinColumn(name = "publication_id", referencedColumnName = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "group_id"))
    private Set<Group> groups;

    @Column(name = "publication_date_time")
    private LocalDateTime publicationDateTime;

    @OneToMany(mappedBy = "publication", cascade = CascadeType.PERSIST)
    private Set<GroupPostStatus> postStatus;

}
