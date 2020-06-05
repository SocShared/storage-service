package ml.socshared.storage.controller.v1;

import lombok.RequiredArgsConstructor;
import ml.socshared.storage.api.v1.rest.GroupApi;
import ml.socshared.storage.domain.model.GroupModel;
import ml.socshared.storage.domain.request.GroupRequest;
import ml.socshared.storage.domain.response.GroupResponse;
import ml.socshared.storage.entity.Group;
import ml.socshared.storage.service.GroupService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class GroupController implements GroupApi {

    private final GroupService service;

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping(value = "/private/groups/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GroupResponse findById(@PathVariable UUID groupId) {
        return service.findById(groupId);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping(value = "/private/users/{userId}/groups", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<GroupModel> findByUserId(@PathVariable UUID userId,
                                         @Min(0) @NotNull @RequestParam(name = "page", required = false) Integer page,
                                         @Min(0) @Max(100) @NotNull @RequestParam(name = "size", required = false) Integer size) {
        return service.findByUserId(userId, page, size);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping(value = "/private/users/{userId}/groups/social_network/{socialNetwork}/groups", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<GroupModel> findByUserIdAndSocialNetwork(@PathVariable UUID userId, @PathVariable Group.SocialNetwork socialNetwork,
                                                         @Min(0) @NotNull @RequestParam(name = "page", required = false) Integer page,
                                                         @Min(0) @Max(100) @NotNull @RequestParam(name = "size", required = false) Integer size) {
        return service.findByUserIdAndSocialNetwork(userId, socialNetwork, page, size);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping(value = "/private/users/{userId}/groups/facebook/{facebookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GroupResponse findByUserIdAndFacebookId(@PathVariable UUID userId, @PathVariable String facebookId) {
        return service.findByUserIdAndFacebookId(userId, facebookId);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping(value = "/private/users/{userId}/groups/vk/{vkId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GroupResponse findByUserIdAndVkId(@PathVariable UUID userId, @PathVariable String vkId) {
        return service.findByUserIdAndVkId(userId, vkId);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping(value = "/private/groups", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public GroupResponse save(@RequestBody @Valid GroupRequest request) {
        return service.save(request);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @DeleteMapping(value = "/private/groups/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteById(@PathVariable UUID groupId) {
        service.deleteById(groupId);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @DeleteMapping(value = "/private/users/{userId}/groups/facebook/{fbId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteByFBId(@PathVariable UUID userId, @PathVariable String fbId) {
        service.deleteByFbId(userId, fbId);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @DeleteMapping(value = "/private/users/{userId}/groups/vk/{vkId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteByVkId(@PathVariable UUID userId, @PathVariable String vkId) {
        service.deleteByVkId(userId, vkId);
    }
}
