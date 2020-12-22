package com.excellenceict.ocean_erp;

public class CurrentStock_Group_Entity_B {
    String item_Id;
    String item_groupName;

    public CurrentStock_Group_Entity_B(String item_Id, String item_groupName) {
        this.item_Id = item_Id;
        this.item_groupName = item_groupName;
    }

    public String getItem_Id() {
        return item_Id;
    }

    public String getItem_groupName() {
        return item_groupName;
    }

    @Override
    public String toString() {
        return item_groupName;
    }
}
