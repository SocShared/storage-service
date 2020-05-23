package ml.socshared.storage.controller.v1;

import lombok.RequiredArgsConstructor;
import ml.socshared.storage.api.v1.rest.GroupApi;
import ml.socshared.storage.domain.model.GroupModel;
import ml.socshared.storage.domain.request.GroupRequest;
import ml.socshared.storage.domain.response.GroupResponse;
import ml.socshared.storage.entity.Group;
import ml.socshared.storage.service.GroupService;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Validated
public class GroupController implements GroupApi {

    private final GroupService service;

    @GetMapping(value = "/private/groups/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GroupResponse findById(@PathVariable UUID groupId) {
        return service.findById(groupId);
    }

    @GetMapping(value = "/private/users/{userId}/groups", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<GroupModel> findByUserId(@PathVariable UUID userId,
                                                   @NotNull @RequestParam(name = "page", required = false) Integer page,
                                                   @NotNull @RequestParam(name = "size", required = false) Integer size) {
        return service.findByUserId(userId, page, size);
    }

    @GetMapping(value = "/private/users/{userId}/groups/social_network/{socialNetwork}/groups", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<GroupModel> findByUserIdAndSocialNetwork(@PathVariable UUID userId, @PathVariable Group.SocialNetwork socialNetwork,
                                                   @NotNull @RequestParam(name = "page", required = false) Integer page,
                                                   @NotNull @RequestParam(name = "size", required = false) Integer size) {
        return service.findByUserIdAndSocialNetwork(userId, socialNetwork, page, size);
    }

    @GetMapping(value = "/private/users/{userId}/groups/facebook/{facebookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GroupResponse findByUserIdAndFacebookId(@PathVariable UUID userId, @PathVariable String facebookId) {
        return service.findByUserIdAndFacebookId(userId, facebookId);
    }

    @GetMapping(value = "/private/users/{userId}/groups/vk/{vkId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GroupResponse findByUserIdAndVkId(@PathVariable UUID userId, @PathVariable String vkId) {
        return service.findByUserIdAndVkId(userId, vkId);
    }

    @PostMapping(value = "/private/groups", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public GroupResponse save(@RequestBody @Valid GroupRequest request) {
        return service.save(request);
    }

    @DeleteMapping(value = "/private/groups/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteById(@PathVariable UUID groupId) {
        service.deleteById(groupId);
    }
}
