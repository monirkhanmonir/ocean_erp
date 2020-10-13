package com.ocean.orcl;

public class SalesChalan_Result_Entity {
    String chalan_Id;
    String chalan_No;
    String chalan_Date;
    String chalan_Amount;

    public SalesChalan_Result_Entity(String chalan_Id, String chalan_No, String chalan_Date, String chalan_Amount) {
        this.chalan_Id = chalan_Id;
        this.chalan_No = chalan_No;
        this.chalan_Date = chalan_Date;
        this.chalan_Amount = chalan_Amount;
    }

    public String getChalan_Id() {
        return chalan_Id;
    }

    public String getChalan_No() {
        return chalan_No;
    }

    public String getChalan_Date() {
        return chalan_Date;
    }

    public String getChalan_Amount() {
        return chalan_Amount;
    }
}
