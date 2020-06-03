package ml.socshared.storage.service;

import ml.socshared.storage.domain.model.PublicationModel;
import ml.socshared.storage.domain.request.PublicationRequest;
import ml.socshared.storage.domain.response.PublicationResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface PublicationService {

    PublicationResponse save(PublicationRequest request);
    Page<PublicationModel> findNotPublishing(Integer page, Integer size);
    Page<PublicationModel> findPublishingAfter(Long date, Integer page, Integer size);
    Page<PublicationModel> findByGroupId(UUID systemGroupId, Integer page, Integer size);
}
