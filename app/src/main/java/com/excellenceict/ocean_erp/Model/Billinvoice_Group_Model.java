package com.excellenceict.ocean_erp.Model;

public class Billinvoice_Group_Model {
    String itemGroup_Id;
    String itemGroup_Name;

    public Billinvoice_Group_Model(String itemGroup_Id, String itemGroup_Name) {
        this.itemGroup_Id = itemGroup_Id;
        this.itemGroup_Name = itemGroup_Name;
    }

    public String getItemGroup_Id()  {
        return itemGroup_Id;
    }

    public String getItemGroup_Name() {
        return itemGroup_Name;
    }

    @Override
    public String toString() {
        return itemGroup_Name;
    }
}
