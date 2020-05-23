package ml.socshared.storage.entity;

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
    private UUID groupPostId;

    @Column(name = "group_id")
    private UUID groupId;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_status", nullable = false)
    private PostStatus postStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publication_id", referencedColumnName = "publication_id")
    private Publication publication;

    public enum PostStatus {
        DEFERRED,
        PUBLISHED,
        AWAITING,
        NOT_SUCCESSFUL,
    }

    public GroupPostStatus() {
        postStatus = PostStatus.AWAITING;
    }

}
