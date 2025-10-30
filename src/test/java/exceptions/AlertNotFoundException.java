package exceptions;

public class AlertNotFoundException extends AutomationException {
    private final String className;
    private final int timeout;
    public AlertNotFoundException(String component, String className, int timeout, String message) {
        super(component, message);
        this.className = className;
        this.timeout = timeout;
    }

    public String getClassName() {
        return className;
    }

    public int getTimeout() {
        return timeout;
    }
}
