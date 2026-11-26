package taxi.patterns.adapter;

import taxi.legacy.LegacySmsGateway;
import taxi.notification.NotificationService;

import java.util.Map;

public class SmsNotificationAdapter implements NotificationService {
    private static final int NORMAL_PRIORITY = 1;
    private static final String UNKNOWN_NUMBER = "unknown";

    private final LegacySmsGateway gateway;        // the adaptee
    private final Map<String, String> phoneBook;   // name -> phone number

    public SmsNotificationAdapter(LegacySmsGateway gateway, Map<String, String> phoneBook) {
        this.gateway = gateway;
        this.phoneBook = phoneBook;
    }

    @Override
    public void notify(String recipient, String message) {
        String phone = phoneBook.getOrDefault(recipient, UNKNOWN_NUMBER);
        int code = gateway.sendSms(phone, message, NORMAL_PRIORITY);   // translate the call
        if (code != 0) {
            throw new IllegalStateException("SMS gateway error code " + code);   // translate the result
        }
    }
}