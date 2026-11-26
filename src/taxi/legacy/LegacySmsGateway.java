package taxi.legacy;

/** Simulates a third-party SMS SDK. We do NOT own this class and never change it. */
public class LegacySmsGateway {
    /** @return 0 on success, any other number is an error code */
    public int sendSms(String phoneNumber, String text, int priority) {
        System.out.println("[LEGACY SMS] to=" + phoneNumber + " priority=" + priority + " text=" + text);
        return 0;
    }
}