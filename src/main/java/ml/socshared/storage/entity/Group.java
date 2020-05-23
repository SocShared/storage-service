package ml.socshared.storage.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "groups", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "facebook_id", "vk_id"})})
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

    @Column(name = "facebook_id")
    private String facebookId;

    @Column(name = "vk_id")
    private String vkId;

    @JsonBackReference
    @ManyToMany(mappedBy = "groups", fetch = FetchType.LAZY)
    private Set<Publication> publications;

    public enum SocialNetwork {
        @JsonProperty("VK")
        VK,
        @JsonProperty("FB")
        FACEBOOK
    }
}
