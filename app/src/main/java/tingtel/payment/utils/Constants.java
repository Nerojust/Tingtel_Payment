package tingtel.payment.utils;

import tingtel.payment.BuildConfig;

public class Constants {

    public static final long VIBRATOR_INTEGER = 100;
    public static final int MINIMUM_DIGIT_PASSWORD = 5;
    public static final int MINIMUM_PHONE_NUMBER_DIGITS = 11;

    public static final String BASE_URL_TINGTEL = BuildConfig.PUBLIC_DEVELOPMENT + "/tingtel/api/";
    public static final String USERNAME = "tingtel";
    public static final String PASSWORD = BuildConfig.HEADER_PASSWORD;

    public static final long CONNECTION_TIMEOUT = 1;
    public static final long READ_TIMEOUT = 30;
    public static final long WRITE_TIMEOUT = 30;

    //This error code signifies Invalid user credentials
    public static final String INVALID_CREDENTIALS = "9996";

    //This error code signifies server error
    public static final String SERVER_ERROR = "9997";

    //This error code signifies Invalid hash key
    public static final String INVALID_HASH_KEY = "9998";

    //This error code signifies user already exists
    public static final String USER_ALREADY_EXISTS = "9999";

    //This error code signifies successful
    public static final String SUCCESS = "0000";

    public static final String TAG = "tingtel_tag";

    public static final String TINGTEL_MTN = "08133469004";
    public static final String TINGTEL_AIRTEL = "09048120037";
    public static final String TINGTEL_9MOBILE = "08174612405";
    public static final String TINGTEL_GLO = "09058815819";

    public static final int TINGTEL_PERCENT = 10;
}
