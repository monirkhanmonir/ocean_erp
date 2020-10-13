package com.ocean.orcl;

public class ACC_View_Voucher_Result_Entity {
    String date;
    String voucher;
    String ref;
    String tr_type;
    String Amount;
    String note;
    String totalVoucher_amount;

    public ACC_View_Voucher_Result_Entity(String date, String voucher, String ref, String tr_type, String amount, String note,String totalVoucher_amount) {
        this.date = date;
        this.voucher = voucher;
        this.ref = ref;
        this.tr_type = tr_type;
        Amount = amount;
        this.note = note;
        this.totalVoucher_amount =totalVoucher_amount;
    }

    public String getDate() {
        return date;
    }

    public String getVoucher() {
        return voucher;
    }

    public String getRef() {
        return ref;
    }

    public String getTr_type() {
        return tr_type;
    }

    public String getAmount() {
        return Amount;
    }

    public String getNote() {
        return note;
    }

    public String getTotalVoucher_amount() {
        return totalVoucher_amount;
    }
}
