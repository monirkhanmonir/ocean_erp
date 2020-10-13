package com.ocean.orcl;

public class Billinvoice_Group_Entity {
    String itemGroup_Id;
    String itemGroup_Name;

    public Billinvoice_Group_Entity(String itemGroup_Id, String itemGroup_Name) {
        this.itemGroup_Id = itemGroup_Id;
        this.itemGroup_Name = itemGroup_Name;
    }

    public String getItemGroup_Id() {
        return itemGroup_Id;
    }

    public String getItemGroup_Name() {
        return itemGroup_Name;
    }
}
