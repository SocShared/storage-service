package ml.socshared.storage.controller.v1;

import lombok.RequiredArgsConstructor;
import ml.socshared.storage.domain.model.PublicationModel;
import ml.socshared.storage.domain.request.PublicationRequest;
import ml.socshared.storage.domain.response.PublicationResponse;
import ml.socshared.storage.service.PublicationService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class PublicationController {

    private final PublicationService publicationService;

    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping(value = "/private/publications", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PublicationResponse save(@RequestBody @Valid PublicationRequest request) {
        return publicationService.save(request);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping(value = "/private/publications/status/not_publishing")
    public Page<PublicationModel> findNotPublishing(@NotNull @RequestParam(name = "page", required = false) Integer page,
                                                    @NotNull @RequestParam(name = "size", required = false) Integer size) {
        return publicationService.findNotPublishing(page, size);
    }

}
