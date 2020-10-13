package com.ocean.orcl;

public class Bill_Result_Entity {
    String invoice_Id;
    String invoide_No;
    String invoice_Date;
    String invoice_Amount;

    public Bill_Result_Entity(String invoice_Id, String invoide_No, String invoice_Date, String invoice_Amount) {
        this.invoice_Id = invoice_Id;
        this.invoide_No = invoide_No;
        this.invoice_Date = invoice_Date;
        this.invoice_Amount = invoice_Amount;
    }

    public String getInvoice_Id() {
        return invoice_Id;
    }

    public String getInvoide_No() {
        return invoide_No;
    }

    public String getInvoice_Date() {
        return invoice_Date;
    }

    public String getInvoice_Amount() {
        return invoice_Amount;
    }
}
