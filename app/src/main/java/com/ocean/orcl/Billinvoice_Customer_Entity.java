package com.ocean.orcl;

public class Billinvoice_Customer_Entity {
    String customer_Id;
    String customer_Name;

    public Billinvoice_Customer_Entity(String customer_Id, String customer_Name) {
        this.customer_Id = customer_Id;
        this.customer_Name = customer_Name;
    }

    public String getCustomer_Id() {
        return customer_Id;
    }

    public String getCustomer_Name() {
        return customer_Name;
    }
}
