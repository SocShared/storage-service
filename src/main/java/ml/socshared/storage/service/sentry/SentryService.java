package ml.socshared.storage.service.sentry;

import io.sentry.Sentry;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SentryService {

    public SentryService() {
        Sentry.init();
    }

    public void logException(Throwable exc) {
        Sentry.capture(exc);
        Sentry.clearContext();
    }

    public void logMessage(String message, Map<String, String> tags, Map<String, Object> extras) {
        addTagsAndExtrasToSentryContext(tags, extras);
        Sentry.capture(message);
    }

    private void addTagsAndExtrasToSentryContext(Map<String, String> tags, Map<String, Object> extras) {
        if (tags != null) {
            for (Map.Entry<String, String> tag : tags.entrySet()) {
                Sentry.getContext().addTag(tag.getKey(), tag.getValue());
            }
        }

        if (extras != null) {
            for (Map.Entry<String, Object> extra : extras.entrySet()) {
                Sentry.getContext().addExtra(extra.getKey(), extra.getValue());
            }
        }
    }
}
