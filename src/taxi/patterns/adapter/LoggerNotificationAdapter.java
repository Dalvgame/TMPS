package taxi.patterns.adapter;

import taxi.notification.NotificationService;

import java.util.logging.Logger;

public class LoggerNotificationAdapter implements NotificationService {
    private final Logger logger;   // java.util.logging.Logger: code we did not write

    public LoggerNotificationAdapter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void notify(String recipient, String message) {
        logger.info("To " + recipient + ": " + message);
    }
}