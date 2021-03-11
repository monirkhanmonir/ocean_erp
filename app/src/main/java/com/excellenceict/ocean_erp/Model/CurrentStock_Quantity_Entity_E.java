package com.excellenceict.ocean_erp.Model;

public class CurrentStock_Quantity_Entity_E {
    String qty;
    String qty1;

    public CurrentStock_Quantity_Entity_E(String qty, String qty1) {
        this.qty = qty;
        this.qty1 = qty1;
    }

    public String getQty() {
        return qty;
    }

    public String getQty1() {
        return qty1;
    }

    @Override
    public String toString() {
        return qty1;
    }
}
