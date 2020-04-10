package tingtel.payment.utils;

import android.content.SharedPreferences;

import static tingtel.payment.utils.MyApplication.getSharedPreferencesCustomer;

public class SessionManager {

    private static final String INTRO_STATUS = "IsIntroOpened";
    private static final String LOGIN_STATUS = "LoginStatus";
    private static final String NETWORK_NAME_Sim_one = "NETWORK NAME";
    private static final String NUMBER_OF_SIMS_ON_DEVICE = "NUMBER_OF_SIMS_ON_DEVICE";
    private static final String SIM_SERIAL = "SIM_SERIAL";
    private static final String SIM_STATUS = "SIM_STATUS";
    private static final String NETWORK_NAME_Sim_two = "NETWORK_NAME_Sim_two";
    private static final String NUMBER_OF_SIMS_ON_DEVICE1 = "NUMBER_OF_SIMS_ON_DEVICE1";
    private static final String SIM_SERIAL1 = "SIM_SERIAL1";

    private static final String NUM_OF_SIMS_FOUND = "NUM_OF_SIMS_FOUND";
    private static final String IS_IT_FROM_TRANSFER_ACTIVITY = "IS_IT_FROM_TRANSFER_ACTIVITY";

    private static final String RECEIVER_STATUS = "RECEIVER_STATUS";
    private static final String IS_LOGIN = "IS_LOGIN";
    private static final String IS_REGISTERED = "IS_REGISTERED";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private static final String PHONE_NUMBER1 = "PHONE_NUMBER1";
    private static final String SCANNED_CODE = "SCANNED_CODE";
    private static final String QRIMAGE = "QRIMAGE";
    private static final String OnBoardStatus = "ONBOARD_STATUS";
    private static final String APPSTATE = "APP_STATE";
    private static final String NO_OF_REGISTERED_SIM = "NO_OF_REGISTERED_SIM";
    private static final String CLICKED_NETWORK = "CLICKED_NETWORK";
    private static final String SELECTED_RV_NETWORK = "SELECTED_RV_NETWORK";
    private static final String RECEIVER_PHONE_NUMBER = "RECEIVER_PHONE_NUMBER";
    private static final String SENDER_PHONE_NUMBER = "SENDER_PHONE_NUMBER";
    private static final String AMOUNT = "AMOUNT";
    private static final String LONG = "LONG";
    private static final String LAT = "LAT";
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String EMAIL_ADDRESS = "EMAIL_ADDRESS";
    private static final String USERNAME = "USERNAME";
    private static final String USER_NETWORK = "USER_NETWORK";
    private static final String REG_JSON = "REG_JSON";
    private static final String LOGIN_OBJECT = "LOGIN_OBJECT";
    private static final String CHECKED = "CHECKED";
    private static final String USER_NETWORK1 = "USER_NETWORK1";
    private static final String PASSWORD_JSON = "PASSWORD_JSON";
    private static final String EMAIL_JSON = "EMAIL_JSON";
    private static final String ISSUE_JSON = "ISSUE_JSON";
    private static final String EMAIL_FROM_LOGIN = "EMAIL_FROM_LOGIN";
    private static final String NUMBER_FROM_LOGIN = "NUMBER_FROM_LOGIN";
    private static final String SIM_NUMBER = "SIM_NUMBER";
    private static final String ENCRYPTED_IV = "ENCRYPTED_IV";
    private static final String ENCRYPTED_PASSWORD = "ENCRYPTED_PASSWORD";
    private static final String PASSWORD_ = "PASSWORD_";
    private static final String CREDIT = "CREDIT";
    private static final String OTP = "OTP";
    private static final String TXN = "TXN";
    private static final String OTP_JSON = "OTP_JSON";
    private static final String SUCCESS_PAGE = "SUCCESS_PAGE";
    private static final String DASHBOARD = "DASHBOARD";
    private static final String SIM_ONE_NUMBER_FROM_LOGIN = "SIM_ONE_NUMBER_FROM_LOGIN";
    private static final String SIM_TWO_NUMBER_FROM_LOGIN = "SIM_TWO_NUMBER_FROM_LOGIN";
    private static final String SIM_THREE_NUMBER_FROM_LOGIN = "SIM_THREE_NUMBER_FROM_LOGIN";
    private static final String SIM_FOUR_NUMBER_FROM_LOGIN = "SIM_FOUR_NUMBER_FROM_LOGIN";

    private static final String SERIAL_1_FROM_LOGIN = "SERIAL_1_FROM_LOGIN";
    private static final String SERIAL_2_FROM_LOGIN = "SERIAL_2_FROM_LOGIN";
    private static final String SERIAL_3_FROM_LOGIN = "SERIAL_3_FROM_LOGIN";
    private static final String SERIAL_4_FROM_LOGIN = "SERIAL_4_FROM_LOGIN";

    private static final String SIM_1_NETWORK_FROM_LOGIN = "SIM_1_NETWORK_FROM_LOGIN";
    private static final String SIM_2_NETWORK_FROM_LOGIN = "SIM_2_NETWORK_FROM_LOGIN";
    private static final String SIM_3_NETWORK_FROM_LOGIN = "SIM_3_NETWORK_FROM_LOGIN";
    private static final String SIM_4_NETWORK_FROM_LOGIN = "SIM_4_NETWORK_FROM_LOGIN";
    private static final String ADD_SIM = "ADD_SIM";
    private static final String SERIAL_MATCH = "SERIAL_MATCH";


    private final SharedPreferences pref = getSharedPreferencesCustomer();

    public void clearSharedPreferences() {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(FIRST_NAME);
        editor.remove(LAST_NAME);
        editor.remove(EMAIL_ADDRESS);
        editor.remove(USERNAME);
        editor.apply();
    }

    private void setIntPreference(String name, int value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    private void setBooleanPreference(String name, boolean value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    private long getLongPreference(String name) {
        if (pref.contains(name)) {
            return pref.getLong(name, 0);
        } else {
            return 0;
        }
    }

    private void setLongPreference(String name, long value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(name, value);
        editor.apply();
    }


    private void setStringPreference(String name, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(name, value);
        editor.apply();
    }

    private void setFloatPreference(String name, float value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(name, value);
        editor.apply();
    }

    private Integer getIntPreference(String name) {
        if (pref.contains(name)) {
            return pref.getInt(name, 0);
        } else {
            return 0;
        }
    }

    private boolean getBooleanPreference(String name) {
        return pref.contains(name) && pref.getBoolean(name, false);
    }

    private float getFloatPreference(String name) {
        if (pref.contains(name)) {
            return pref.getFloat(name, 0);
        } else {
            return 0;
        }
    }

    private String getStringPreference(String name) {
        if (pref.contains(name)) {
            return pref.getString(name, "");
        } else {
            return null;
        }
    }

    public void startLoginSession() {
        setBooleanPreference(IS_LOGIN, true);
    }

    public boolean isLogin() {
        return getBooleanPreference(IS_LOGIN);
    }

    public void logout() {
        setBooleanPreference(IS_LOGIN, false);
    }


    public String getSimOneNetworkName() {
        return getStringPreference(NETWORK_NAME_Sim_one);
    }

    public void setSimOneNetworkName(String networkname) {
        setStringPreference(NETWORK_NAME_Sim_one, networkname);
    }

    public String getSimTwoNetworkName() {
        return getStringPreference(NETWORK_NAME_Sim_two);
    }

    public void setSimTwoNetworkName(String networkname) {
        setStringPreference(NETWORK_NAME_Sim_two, networkname);
    }

    public String getSimOnePhoneNumber() {
        return getStringPreference(PHONE_NUMBER);
    }

    public void setSimOnePhoneNumber(String phoneNumber) {
        setStringPreference(PHONE_NUMBER, phoneNumber);
    }

    public String getSimTwoPhoneNumber() {
        return getStringPreference(PHONE_NUMBER1);
    }

    public void setSimTwoPhoneNumber(String phoneNumber) {
        setStringPreference(PHONE_NUMBER1, phoneNumber);
    }

    public int getNumberOfSimsOnTheDevice() {
        return getIntPreference(NUMBER_OF_SIMS_ON_DEVICE);
    }

    public void setNumberOfSimsOnTheDevice(int numberOfSimsOnTheDevice) {
        setIntPreference(NUMBER_OF_SIMS_ON_DEVICE, numberOfSimsOnTheDevice);
    }

    public String getSimSerialICCID() {
        return getStringPreference(SIM_SERIAL);
    }

    public void setSimSerialICCID(String iccid) {
        setStringPreference(SIM_SERIAL, iccid);
    }


    public int getNumberOfSimsOnTheDevice1() {
        return getIntPreference(NUMBER_OF_SIMS_ON_DEVICE1);
    }

    public void setNumberOfSimsOnTheDevice1(int numberOfSimsOnTheDevice) {
        setIntPreference(NUMBER_OF_SIMS_ON_DEVICE1, numberOfSimsOnTheDevice);
    }

    public String getSimSerialICCID1() {
        return getStringPreference(SIM_SERIAL1);
    }

    public void setSimSerialICCID1(String iccid) {
        setStringPreference(SIM_SERIAL1, iccid);
    }


    public String getSimStatus() {
        return getStringPreference(SIM_STATUS);
    }

    public void setSimStatus(String simstatus) {
        setStringPreference(SIM_STATUS, simstatus);
    }

    public boolean getOnboardStatus() {

        return getBooleanPreference(OnBoardStatus);
    }

    public void setOnBoardStatus(Boolean status) {
        setBooleanPreference(OnBoardStatus, status);
    }

    public int getTotalNumberOfSimsDetectedOnDevice() {
        return getIntPreference(NUM_OF_SIMS_FOUND);
    }

    public void setTotalNumberOfSimsDetectedOnDevice(int numberOfSimsOnTheDevice) {
        setIntPreference(NUM_OF_SIMS_FOUND, numberOfSimsOnTheDevice);
    }

    public boolean getIsRequestFromTransferActivity() {
        return getBooleanPreference(IS_IT_FROM_TRANSFER_ACTIVITY);
    }

    public void setIsRequestFromTransferActivity(boolean b) {
        setBooleanPreference(IS_IT_FROM_TRANSFER_ACTIVITY, b);
    }


    public String getAppState() {

        return getStringPreference(APPSTATE);
    }

    public void setAppstate(String AppState) {
        setStringPreference(APPSTATE, AppState);
    }


    public Boolean getIsRegistered() {
        return getBooleanPreference(IS_REGISTERED);
    }

    public void setIsRegistered(Boolean isRegistered) {
        setBooleanPreference(IS_REGISTERED, isRegistered);
    }

    public String getNoOfRegisteredSim() {

        return getStringPreference(NO_OF_REGISTERED_SIM);
    }

    public void setNoOfRegisteredSim(String NoOfRegisteredSim) {
        setStringPreference(NO_OF_REGISTERED_SIM, NoOfRegisteredSim);
    }

    public String getScannedCodeResult() {
        return getStringPreference(SCANNED_CODE);
    }

    public void setScannedCodeResult(String code) {
        setStringPreference(SCANNED_CODE, code);
    }

    public String getImageStoragePath() {
        return getStringPreference(QRIMAGE);
    }

    public void setImageStoragePath(String barcodeImage) {
        setStringPreference(QRIMAGE, barcodeImage);
    }

    public String getClickedNetwork() {
        return getStringPreference(CLICKED_NETWORK);
    }

    public void setClickedNetwork(String network) {
        setStringPreference(CLICKED_NETWORK, network);
    }

    public String getSelectedRvNetwork() {
        return getStringPreference(SELECTED_RV_NETWORK);
    }

    public void setSelectedRvNetwork(String network) {
        setStringPreference(SELECTED_RV_NETWORK, network);
    }

    public String getReceiverPhoneNumber() {
        return getStringPreference(RECEIVER_PHONE_NUMBER);
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        setStringPreference(RECEIVER_PHONE_NUMBER, receiverPhoneNumber);
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        setStringPreference(SENDER_PHONE_NUMBER, senderPhoneNumber);
    }

    public String getSenderPhonerNumber() {
        return getStringPreference(SENDER_PHONE_NUMBER);
    }

    public String getAmount() {
        return getStringPreference(AMOUNT);
    }

    public void setAmount(String amount) {
        setStringPreference(AMOUNT, amount);
    }

    public String getLongitude() {
        return getStringPreference(LONG);
    }

    public void setLongitude(String longitude) {
        setStringPreference(LONG, longitude);
    }

    public String getLatitude() {
        return getStringPreference(LAT);
    }

    public void setLatitude(String latitude) {
        setStringPreference(LAT, latitude);
    }

    public String getFirstName() {
        return getStringPreference(FIRST_NAME);
    }

    public void setFirstNameFromLogin(String firstname) {
        setStringPreference(FIRST_NAME, firstname);
    }

    public String getLastNameFromLogin() {
        return getStringPreference(LAST_NAME);
    }

    public void setLastName(String lastName) {
        setStringPreference(LAST_NAME, lastName);
    }

    public void setUsername(String username) {
        setStringPreference(USERNAME, username);
    }

    public String getUserame() {
        return getStringPreference(USERNAME);
    }

    public String getPassword() {
        return getStringPreference(PASSWORD_);
    }

    public void setPassword(String password) {
        setStringPreference(PASSWORD_, password);
    }

    public String getEmailAddress() {
        return getStringPreference(EMAIL_ADDRESS);
    }

    public void setEmailAddress(String emailAddress) {
        setStringPreference(EMAIL_ADDRESS, emailAddress);
    }

    public String getUserNetwork() {
        return getStringPreference(USER_NETWORK);
    }

    public void setUserNetwork(String selectedSpinnerNetwork) {
        setStringPreference(USER_NETWORK, selectedSpinnerNetwork);
    }

    public String getUserNetwork1() {
        return getStringPreference(USER_NETWORK1);
    }

    public void setUserNetwork1(String selectedSpinnerNetwork) {
        setStringPreference(USER_NETWORK1, selectedSpinnerNetwork);
    }


    public String getRegistrationJsonObject() {
        return getStringPreference(REG_JSON);
    }

    public void setRegistrationJsonObject(String regObject) {
        setStringPreference(REG_JSON, regObject);
    }

    public String getLoginJsonObject() {
        return getStringPreference(LOGIN_OBJECT);
    }

    public void setLoginJsonObject(String login) {
        setStringPreference(LOGIN_OBJECT, login);
    }

    public boolean getRememberLoginCheck() {
        return getBooleanPreference(CHECKED);
    }

    public void setRememberLoginCheck(boolean checked) {
        setBooleanPreference(CHECKED, checked);
    }

    public String getPasswordJsonObject() {
        return getStringPreference(PASSWORD_JSON);
    }

    public void setPasswordJsonObject(String jsonObject) {
        setStringPreference(PASSWORD_JSON, jsonObject);
    }

    public String getEmailJsonObject() {
        return getStringPreference(EMAIL_JSON);
    }

    public void setEmailJsonObject(String jsonObject) {
        setStringPreference(EMAIL_JSON, jsonObject);
    }

    public String getReportIssueJsonObject() {
        return getStringPreference(ISSUE_JSON);
    }

    public void setReportIssueJsonObject(String jsonObject) {
        setStringPreference(ISSUE_JSON, jsonObject);
    }

    public String getNumberFromLogin() {
        return getStringPreference(NUMBER_FROM_LOGIN);
    }

    public void setNumberFromLogin(String phone) {
        setStringPreference(NUMBER_FROM_LOGIN, phone);
    }

    public String getEmailFromLogin() {
        return getStringPreference(EMAIL_FROM_LOGIN);
    }

    public void setEmailFromLogin(String emailFromLogin) {
        setStringPreference(EMAIL_FROM_LOGIN, emailFromLogin);
    }

    public int getWhichSimWasClicked() {
        return getIntPreference(SIM_NUMBER);
    }

    public void setWhichSimWasClicked(int simNo) {
        setIntPreference(SIM_NUMBER, simNo);
    }

    public String getEncryptionIV() {
        return getStringPreference(ENCRYPTED_IV);
    }

    public void setEncryptionIV(String encodeToString) {
        setStringPreference(ENCRYPTED_IV, encodeToString);
    }

    public String getEncryptedPassword() {
        return getStringPreference(ENCRYPTED_PASSWORD);
    }

    public void setEncryptedPassword(String encryptedPasswordString) {
        setStringPreference(ENCRYPTED_PASSWORD, encryptedPasswordString);
    }

    public String getCreditRequestJsonObject() {
        return getStringPreference(CREDIT);
    }

    public void setCreditRequestJsonObject(String credit) {
        setStringPreference(CREDIT, credit);
    }

    public String getOTP() {
        return getStringPreference(OTP);
    }

    public void setOTP(String generatedOTP) {
        setStringPreference(OTP, generatedOTP);
    }

    public String getTransactionReference() {
        return getStringPreference(TXN);
    }

    public void setTransactionReference(String random) {
        setStringPreference(TXN, random);
    }

    public String getOTPJsonObject() {
        return getStringPreference(OTP_JSON);
    }

    public void setOTPJsonObject(String jsonObject) {
        setStringPreference(OTP_JSON, jsonObject);
    }

    public boolean getComingFromSuccess() {
        return getBooleanPreference(SUCCESS_PAGE);
    }

    public void setComingFromSuccess(boolean b) {
        setBooleanPreference(SUCCESS_PAGE, b);
    }

    public boolean getComingFromDashboard() {
        return getBooleanPreference(DASHBOARD);
    }

    public void setComingFromDashboard(boolean b) {
        setBooleanPreference(DASHBOARD, b);
    }

    public String getSimOnePhoneNumberFromLogin() {
        return getStringPreference(SIM_ONE_NUMBER_FROM_LOGIN);
    }

    public void setSimOnePhoneNumberFromLogin(String phone) {
        setStringPreference(SIM_ONE_NUMBER_FROM_LOGIN, phone);
    }

    public String getSimTwoPhoneNumberFromLogin() {
        return getStringPreference(SIM_TWO_NUMBER_FROM_LOGIN);
    }

    public void setSimTwoPhoneNumberFromLogin(String phone) {
        setStringPreference(SIM_TWO_NUMBER_FROM_LOGIN, phone);
    }

    public String getSimThreePhoneNumberFromLogin() {
        return getStringPreference(SIM_THREE_NUMBER_FROM_LOGIN);
    }

    public void setSimThreePhoneNumberFromLogin(String phone) {
        setStringPreference(SIM_THREE_NUMBER_FROM_LOGIN, phone);
    }

    public String getSimFourPhoneNumberFromLogin() {
        return getStringPreference(SIM_FOUR_NUMBER_FROM_LOGIN);
    }

    public void setSimFourPhoneNumberFromLogin(String phone) {
        setStringPreference(SIM_FOUR_NUMBER_FROM_LOGIN, phone);
    }

    public void setSimOneSerialICCIDFromLogin(String serial) {
        setStringPreference(SERIAL_1_FROM_LOGIN, serial);
    }

    public String getSimOneSerialICCIDfromLogin() {
        return getStringPreference(SERIAL_1_FROM_LOGIN);
    }

    public void setSimTwoSerialICCIDFromLogin(String serial) {
        setStringPreference(SERIAL_2_FROM_LOGIN, serial);
    }

    public String getSimTwoSerialICCIDfromLogin() {
        return getStringPreference(SERIAL_2_FROM_LOGIN);
    }

    public void setSimThreeSerialICCIDFromLogin(String serial) {
        setStringPreference(SERIAL_3_FROM_LOGIN, serial);
    }

    public String getSimThreeSerialICCIDfromLogin() {
        return getStringPreference(SERIAL_3_FROM_LOGIN);
    }

    public void setSimFourSerialICCIDFromLogin(String serial) {
        setStringPreference(SERIAL_4_FROM_LOGIN, serial);
    }

    public String getSimFourSerialICCIDfromLogin() {
        return getStringPreference(SERIAL_4_FROM_LOGIN);
    }

    public String getNetworkNameSimOneFromLogin() {
        return getStringPreference(SIM_1_NETWORK_FROM_LOGIN);
    }

    public void setNetworkNameSimOneFromLogin(String network) {
        setStringPreference(SIM_1_NETWORK_FROM_LOGIN, network);
    }

    public String getNetworkNameSimTwoFromLogin() {
        return getStringPreference(SIM_2_NETWORK_FROM_LOGIN);
    }

    public void setNetworkNameSimTwoFromLogin(String network) {
        setStringPreference(SIM_2_NETWORK_FROM_LOGIN, network);
    }

    public String getNetworkNameSimThreeFromLogin() {
        return getStringPreference(SIM_3_NETWORK_FROM_LOGIN);
    }

    public void setNetworkNameSimThreeFromLogin(String network) {
        setStringPreference(SIM_3_NETWORK_FROM_LOGIN, network);
    }

    public String getNetworkNameSimFourFromLogin() {
        return getStringPreference(SIM_4_NETWORK_FROM_LOGIN);
    }

    public void setNetworkNameSimFourFromLogin(String network) {
        setStringPreference(SIM_4_NETWORK_FROM_LOGIN, network);
    }

    public String getAddSimJsonObject() {
        return getStringPreference(ADD_SIM);
    }

    public void setAddSimJsonObject(String jsonObject) {
        setStringPreference(ADD_SIM, jsonObject);
    }

    public boolean getSerialsDontMatchAnyOnDevice() {
        return getBooleanPreference(SERIAL_MATCH);
    }

    public void setSerialsDontMatchAnyOnDevice(boolean b) {
        setBooleanPreference(SERIAL_MATCH, b);
    }
}
