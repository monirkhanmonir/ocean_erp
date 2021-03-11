package com.excellenceict.ocean_erp.Model;

public class Leave_LogDetails_Model {
    private String type;
    private String allowed;
    private String count;
    private String balance;

    public Leave_LogDetails_Model(String type, String allowed, String count, String balance) {
        this.type = type;
        this.allowed = allowed;
        this.count = count;
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public String getAllowed() {
        return allowed;
    }

    public String getCount() {
        return count;
    }

    public String getBalance() {
        return balance;
    }
}
