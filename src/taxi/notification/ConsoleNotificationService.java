package taxi.notification;

public class ConsoleNotificationService implements NotificationService {
    @Override
    public void notify(String recipient, String message) {
        System.out.println("[Notification to " + recipient + "] " + message);
    }
}