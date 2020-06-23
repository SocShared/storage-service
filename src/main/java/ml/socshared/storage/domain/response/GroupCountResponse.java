package ml.socshared.storage.domain.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupCountResponse {

    private long facebookGroupCount;
    private long vkGroupCount;

}
