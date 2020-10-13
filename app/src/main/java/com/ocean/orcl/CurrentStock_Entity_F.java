package com.ocean.orcl;

public class CurrentStock_Entity_F {
    String lot_no;
    String exp_date;
    String qty;

    public CurrentStock_Entity_F(String lot_no, String exp_date, String qty) {
        this.lot_no = lot_no;
        this.exp_date = exp_date;
        this.qty = qty;
    }

    public String getLot_no() {
        return lot_no;
    }

    public String getExp_date() {
        return exp_date;
    }

    public String getQty() {
        return qty;
    }
}
