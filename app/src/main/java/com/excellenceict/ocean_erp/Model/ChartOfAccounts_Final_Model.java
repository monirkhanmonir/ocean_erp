package com.excellenceict.ocean_erp.Model;

import java.io.Serializable;

public class ChartOfAccounts_Final_Model implements Serializable {
    String subHeaderCode;
    String subHeaderName;
    String description;


    public ChartOfAccounts_Final_Model( String subHeaderCode, String subHeaderName, String description) {
        this.subHeaderCode = subHeaderCode;
        this.subHeaderName = subHeaderName;
        this.description = description;
    }

    public String getSubHeaderCode() {
        return subHeaderCode;
    }

    public void setSubHeaderCode(String subHeaderCode) {
        this.subHeaderCode = subHeaderCode;
    }

    public String getSubHeaderName() {
        return subHeaderName;
    }

    public void setSubHeaderName(String subHeaderName) {
        this.subHeaderName = subHeaderName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
