package com.ocean.orcl;

public class Chalan_Report_Summary_Entity {
    String chalanItem_name;
    String chalanSell_qty;
    String chalanMu_name;
    String chalan_amount;

    public Chalan_Report_Summary_Entity(String chalanItem_name, String chalanSell_qty, String chalanMu_name, String chalan_amount) {
        this.chalanItem_name = chalanItem_name;
        this.chalanSell_qty = chalanSell_qty;
        this.chalanMu_name = chalanMu_name;
        this.chalan_amount = chalan_amount;
    }

    public String getChalanItem_name() {
        return chalanItem_name;
    }

    public String getChalanSell_qty() {
        return chalanSell_qty;
    }

    public String getChalanMu_name() {
        return chalanMu_name;
    }

    public String getChalan_amount() {
        return chalan_amount;
    }
}
