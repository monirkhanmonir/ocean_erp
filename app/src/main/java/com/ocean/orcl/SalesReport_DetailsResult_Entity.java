package com.ocean.orcl;

public class SalesReport_DetailsResult_Entity {
    String item_name;
    String sale_qty;
    String mu_name;
    String contsct_name;
    String invoice_date;
    String invoice_no;
    String sales_amount;

    public SalesReport_DetailsResult_Entity(String item_name, String sale_qty, String mu_name, String contsct_name, String invoice_date, String invoice_no, String sales_amount) {
        this.item_name = item_name;
        this.sale_qty = sale_qty;
        this.mu_name = mu_name;
        this.contsct_name = contsct_name;
        this.invoice_date = invoice_date;
        this.invoice_no = invoice_no;
        this.sales_amount = sales_amount;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getSale_qty() {
        return sale_qty;
    }

    public String getMu_name() {
        return mu_name;
    }

    public String getContsct_name() {
        return contsct_name;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public String getSales_amount() {
        return sales_amount;
    }
}
