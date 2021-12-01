package com.ynot.jomgaa.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginUser implements Serializable {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("ref_code")
    @Expose
    private String refCode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mail_id")
    @Expose
    private String mailId;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("erorr_message")
    @Expose
    private String erorrMessage;

    @SerializedName("otp")
    @Expose
    private String otp;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getErorrMessage() {
        return erorrMessage;
    }

    public void setErorrMessage(String erorrMessage) {
        this.erorrMessage = erorrMessage;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
