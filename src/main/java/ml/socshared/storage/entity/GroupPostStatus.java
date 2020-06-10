package ml.socshared.storage.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
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
    @Column(name = "post_id")
    @JsonIgnore
    private UUID postId;

    @Column(name = "group_id")
    private UUID groupId;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_status", nullable = false)
    private PostStatus postStatus;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "publication_id", referencedColumnName = "publication_id")
    private Publication publication;

    @Column(name = "status_text")
    private String statusText;

    @Column(name = "social_network", nullable = false)
    @Enumerated(EnumType.STRING)
    private Group.SocialNetwork socialNetwork;

    @Column(name = "post_facebook_id")
    private String postFacebookId;

    @Column(name = "post_vk_id")
    private String postVkId;

    @Column(name = "group_facebook_id")
    private String groupFacebookId;

    @Column(name = "group_vk_id")
    private String groupVkId;

    public enum PostStatus {
        @JsonProperty("published")
        PUBLISHED,
        @JsonProperty("awaiting")
        AWAITING,
        @JsonProperty("not_successful")
        NOT_SUCCESSFUL,
        @JsonProperty("processing")
        PROCESSING
    }

    public GroupPostStatus() {
        postStatus = PostStatus.AWAITING;
    }
}
