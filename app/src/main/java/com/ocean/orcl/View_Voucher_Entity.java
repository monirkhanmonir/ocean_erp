package com.ocean.orcl;

public class View_Voucher_Entity {
    String tr_type;
    String tr_type_val;

    public View_Voucher_Entity(String tr_type, String tr_type_val) {
        this.tr_type = tr_type;
        this.tr_type_val = tr_type_val;
    }

    public String getTr_type() {
        return tr_type;
    }

    public String getTr_type_val() {
        return tr_type_val;
    }
}
