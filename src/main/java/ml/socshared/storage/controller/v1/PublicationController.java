package ml.socshared.storage.controller.v1;

import lombok.RequiredArgsConstructor;
import ml.socshared.storage.domain.model.PublicationModel;
import ml.socshared.storage.domain.request.PublicationRequest;
import ml.socshared.storage.domain.response.GroupCountResponse;
import ml.socshared.storage.domain.response.PublicationCountResponse;
import ml.socshared.storage.domain.response.PublicationResponse;
import ml.socshared.storage.entity.Publication;
import ml.socshared.storage.service.PublicationService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class PublicationController {

    private final PublicationService publicationService;

    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping(value = "/private/publications")
    public PublicationResponse save(@RequestBody PublicationRequest request) {
        return publicationService.save(request);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping(value = "/private/publications/status/published")
    public Page<Publication> findAfter(@NotNull @RequestParam(name = "after", required = false) Long after,
                                            @Min(0) @NotNull @RequestParam(name = "page", defaultValue = "0") Integer page,
                                            @Min(0) @Max(100) @NotNull @RequestParam(name = "size", defaultValue = "100") Integer size) {
        return publicationService.findPublishingAfter(after, page, size);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping(value = "/private/publications/status/not_publishing")
    public Page<Publication> findNotPublishing(@Min(0) @NotNull @RequestParam(name = "page", defaultValue = "0") Integer page,
                                               @Min(0) @Max(100) @NotNull @RequestParam(name = "size", defaultValue = "100") Integer size) {
        return publicationService.findNotPublishing(page, size);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping(value = "/private/groups/{systemGroupId}/publications")
    public Page<Publication> findByGroupId(@PathVariable UUID systemGroupId,
                                                @Min(0) @NotNull @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                @Min(0) @Max(100) @NotNull @RequestParam(name = "size", defaultValue = "100") Integer size) {
            return publicationService.findByGroupId(systemGroupId, page, size);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping(value = "/private/publications/count")
    public PublicationCountResponse publicationCount() {
        return publicationService.publicationCount();
    }

}
