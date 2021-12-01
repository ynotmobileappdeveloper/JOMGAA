package com.ynot.jomgaa.Model;

public class User {
    private String user_id;
    private String ref_code;
    private String name;
    private String mail_id;
    private String mobile;

    public User(String user_id, String ref_code, String name, String mail_id, String mobile) {
        this.user_id = user_id;
        this.ref_code = ref_code;
        this.name = name;
        this.mail_id = mail_id;
        this.mobile = mobile;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRef_code() {
        return ref_code;
    }

    public void setRef_code(String ref_code) {
        this.ref_code = ref_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail_id() {
        return mail_id;
    }

    public void setMail_id(String mail_id) {
        this.mail_id = mail_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
