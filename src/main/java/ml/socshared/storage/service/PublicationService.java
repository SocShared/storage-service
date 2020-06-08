package ml.socshared.storage.service;

import ml.socshared.storage.domain.request.PublicationRequest;
import ml.socshared.storage.domain.response.PublicationResponse;
import ml.socshared.storage.entity.Publication;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface PublicationService {

    PublicationResponse save(PublicationRequest request);
    Page<Publication> findNotPublishing(Integer page, Integer size);
    Page<PublicationResponse> findPublishingAfter(Long date, Integer page, Integer size);
    Page<Publication> findByGroupId(UUID systemGroupId, Integer page, Integer size);
}
