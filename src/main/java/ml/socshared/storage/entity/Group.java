package ml.socshared.storage.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ml.socshared.storage.entity.base.BaseEntity;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "groups", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "group_facebook_id", "group_vk_id"})})
public class Group extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "group_id")
    private UUID groupId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "social_network", nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialNetwork socialNetwork;

    @Column(name = "group_facebook_id")
    private String groupFacebookId;

    @Column(name = "group_vk_id")
    private String groupVkId;

    @JsonBackReference
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private Set<GroupPostStatus> postStatus;

    public enum SocialNetwork {
        @JsonProperty("VK")
        VK,
        @JsonProperty("FB")
        FACEBOOK
    }
}
