package com.example.bighomework;

public class UserInfo {
    private String phone;
    private String password;
    private String email;

    public UserInfo(String phone, String password, String email)
    {
        this.phone=phone;
        this.password=password;
        this.email=email;
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

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String toString()
    {
        return "userInfo{" + "phone=" + phone + ",password=" + password + ",email" + email;
    }
}
