package exceptions;

import java.time.ZonedDateTime;

public class AutomationException extends RuntimeException {
    private final String component;
    private final ZonedDateTime timestamp;

    public AutomationException(String component, String message) {
        super(message);
        this.component = component;
        this.timestamp = ZonedDateTime.now();
    }

    public String getComponent() {
        return component;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}
