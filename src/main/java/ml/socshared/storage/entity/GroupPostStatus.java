package ml.socshared.storage.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "group_post_status")
public class GroupPostStatus {

    @Id
    @GeneratedValue
    @Column(name = "group_post_id")
    @JsonIgnore
    private UUID groupPostId;

    @Column(name = "group_id")
    private UUID groupId;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_status", nullable = false)
    private PostStatus postStatus;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publication_id", referencedColumnName = "publication_id")
    private Publication publication;

    public enum PostStatus {
        PUBLISHED,
        AWAITING,
        NOT_SUCCESSFUL,
        PROCESSING
    }

    public GroupPostStatus() {
        postStatus = PostStatus.AWAITING;
    }

}
