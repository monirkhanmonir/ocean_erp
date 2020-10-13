package com.ocean.orcl;

public class CurrentStock_ItemName_Entity_C {
    String item_Id;
    String item_Name;

    public CurrentStock_ItemName_Entity_C(String item_Id, String item_Name) {
        this.item_Id = item_Id;
        this.item_Name = item_Name;
    }

    public String getItem_Id() {
        return item_Id;
    }

    public String getItem_Name() {
        return item_Name;
    }

    @Override
    public String toString() {
        return item_Name;
    }
}
