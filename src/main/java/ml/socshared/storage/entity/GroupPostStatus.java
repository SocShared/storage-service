package ml.socshared.storage.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ml.socshared.storage.entity.key.GroupPublicationPK;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "group_post_status")
@IdClass(GroupPublicationPK.class)
public class GroupPostStatus implements Serializable {

    @Id
    @Column(name = "group_id")
    private UUID groupId;

    @Id
    @JsonIgnore
    @Column(name = "publication_id")
    private UUID publicationId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "publication_id", referencedColumnName = "publication_id", insertable = false, updatable = false)
    private Publication publication;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id", referencedColumnName = "group_id", insertable = false, updatable = false)
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_status", nullable = false)
    private PostStatus postStatus;

    @Column(name = "status_text")
    private String statusText;

    @Column(name = "social_network", nullable = false)
    @Enumerated(EnumType.STRING)
    private Group.SocialNetwork socialNetwork;

    @Column(name = "post_facebook_id")
    private String postFacebookId;

    @Column(name = "post_vk_id")
    private String postVkId;

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
