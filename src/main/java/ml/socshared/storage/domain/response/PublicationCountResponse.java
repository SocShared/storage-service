package ml.socshared.storage.domain.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublicationCountResponse {

    private long publishedCount;
    private long notSuccessfulCount;
    private long waitingOrProcessingCount;
    private long vkPublicationCount;
    private long fbPublicationCount;

}
