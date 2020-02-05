package tingtel.payment.utils;

public class AppUtils {

    private static SessionManager sessionManager;

    public static SessionManager getSessionManagerInstance() {
        if (sessionManager == null) {
            sessionManager = new SessionManager();
        }
        return sessionManager;
    }
}
