package tingtel.payment.models.validate_user;


import com.google.gson.annotations.SerializedName;


public class ValidateUserResponse {

    @SerializedName("code")

    private String code;
    @SerializedName("data")

    private Data data;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("id")

        private Integer id;
        @SerializedName("first_name")

        private String firstName;
        @SerializedName("last_name")

        private String lastName;
        @SerializedName("email")

        private String email;
        @SerializedName("phone")

        private String phone;
        @SerializedName("email_verified_at")

        private String emailVerifiedAt;
        @SerializedName("created_at")

        private String createdAt;
        @SerializedName("updated_at")

        private String updatedAt;
        @SerializedName("is_active")

        private Integer isActive;
        @SerializedName("user_network")

        private String userNetwork;
        @SerializedName("username")

        private String username;
        @SerializedName("ip")

        private String ip;
        @SerializedName("log")

        private String log;
        @SerializedName("lat")

        private String lat;
        @SerializedName("latitude_dec")

        private String latitudeDec;
        @SerializedName("nationality")

        private String nationality;
        @SerializedName("phone2")

        private String phone2;
        @SerializedName("sim2_serial")

        private String sim2Serial;
        @SerializedName("sim1_serial")

        private String sim1Serial;
        @SerializedName("sim2_user_network")

        private String sim2UserNetwork;
        @SerializedName("serial_number")

        private String serialNumber;
        @SerializedName("phone3")

        private String phone3;
        @SerializedName("phone4")

        private String phone4;
        @SerializedName("sim3_serial")

        private String sim3Serial;
        @SerializedName("sim4_serial")

        private String sim4Serial;
        @SerializedName("sim3_user_network")

        private String sim3UserNetwork;
        @SerializedName("sim4_user_network")

        private String sim4UserNetwork;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmailVerifiedAt() {
            return emailVerifiedAt;
        }

        public void setEmailVerifiedAt(String emailVerifiedAt) {
            this.emailVerifiedAt = emailVerifiedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Integer getIsActive() {
            return isActive;
        }

        public void setIsActive(Integer isActive) {
            this.isActive = isActive;
        }

        public String getUserNetwork() {
            return userNetwork;
        }

        public void setUserNetwork(String userNetwork) {
            this.userNetwork = userNetwork;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getLog() {
            return log;
        }

        public void setLog(String log) {
            this.log = log;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLatitudeDec() {
            return latitudeDec;
        }

        public void setLatitudeDec(String latitudeDec) {
            this.latitudeDec = latitudeDec;
        }

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

        public String getPhone2() {
            return phone2;
        }

        public void setPhone2(String phone2) {
            this.phone2 = phone2;
        }

        public String getSim2Serial() {
            return sim2Serial;
        }

        public void setSim2Serial(String sim2Serial) {
            this.sim2Serial = sim2Serial;
        }

        public String getSim1Serial() {
            return sim1Serial;
        }

        public void setSim1Serial(String sim1Serial) {
            this.sim1Serial = sim1Serial;
        }

        public String getSim2UserNetwork() {
            return sim2UserNetwork;
        }

        public void setSim2UserNetwork(String sim2UserNetwork) {
            this.sim2UserNetwork = sim2UserNetwork;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public String getPhone3() {
            return phone3;
        }

        public void setPhone3(String phone3) {
            this.phone3 = phone3;
        }

        public String getPhone4() {
            return phone4;
        }

        public void setPhone4(String phone4) {
            this.phone4 = phone4;
        }

        public String getSim3Serial() {
            return sim3Serial;
        }

        public void setSim3Serial(String sim3Serial) {
            this.sim3Serial = sim3Serial;
        }

        public String getSim4Serial() {
            return sim4Serial;
        }

        public void setSim4Serial(String sim4Serial) {
            this.sim4Serial = sim4Serial;
        }

        public String getSim3UserNetwork() {
            return sim3UserNetwork;
        }

        public void setSim3UserNetwork(String sim3UserNetwork) {
            this.sim3UserNetwork = sim3UserNetwork;
        }

        public String getSim4UserNetwork() {
            return sim4UserNetwork;
        }

        public void setSim4UserNetwork(String sim4UserNetwork) {
            this.sim4UserNetwork = sim4UserNetwork;
        }
    }
}

