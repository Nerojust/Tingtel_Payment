package tingtel.payment.utils;

import android.content.SharedPreferences;

import static tingtel.payment.utils.MyApplication.getSharedPreferencesCustomer;

public class SessionManager {

    private static final String INTRO_STATUS = "IsIntroOpened";
    private static final String LOGIN_STATUS = "LoginStatus";
    private static final String NETWORK_NAME = "NETWORK NAME";
    private static final String NUMBER_OF_SIMS_ON_DEVICE = "NUMBER_OF_SIMS_ON_DEVICE";
    private static final String SIM_SERIAL = "SIM_SERIAL";
    private static final String SIM_STATUS = "SIM_STATUS";
    private static final String NETWORK_NAME1 = "NETWORK_NAME1";
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
    private final SharedPreferences pref = getSharedPreferencesCustomer();

    public void clearSharedPreferences(){
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

    public String getNetworkName() {
        return getStringPreference(NETWORK_NAME);
    }

    public void setNetworkName(String networkname) {
        setStringPreference(NETWORK_NAME, networkname);
    }

    public String getSimPhoneNumber() {
        return getStringPreference(PHONE_NUMBER);
    }

    public void setSimPhoneNumber(String phoneNumber) {
        setStringPreference(PHONE_NUMBER, phoneNumber);
    }

    public String getSimPhoneNumber1() {
        return getStringPreference(PHONE_NUMBER1);
    }

    public void setSimPhoneNumber1(String phoneNumber) {
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


    public String getNetworkName1() {
        return getStringPreference(NETWORK_NAME1);
    }

    public void setNetworkName1(String networkname) {
        setStringPreference(NETWORK_NAME1, networkname);
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

    public Boolean getIsLogin() {
        return getBooleanPreference(IS_LOGIN);
    }

    public void setIsLogin(Boolean isLogin) {
        setBooleanPreference(IS_LOGIN, isLogin);
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

    public void setFirstName(String firstname) {
        setStringPreference(FIRST_NAME, firstname);
    }

    public String getLastName() {
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

    public boolean getRememberLoginCheck(){
        return getBooleanPreference(CHECKED);
    }

    public void setRememberLoginCheck(boolean checked) {
        setBooleanPreference(CHECKED, checked);
    }
}
