package notification.confirm;

// Interface for confirmation-style notifications
public interface ConfirmNotifier {
    boolean pushPrompt(String message);
}
