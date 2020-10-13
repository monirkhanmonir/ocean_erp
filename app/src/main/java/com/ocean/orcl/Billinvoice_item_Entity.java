package com.ocean.orcl;

public class Billinvoice_item_Entity {
    String item_id;
    String item_name;

    public Billinvoice_item_Entity(String item_id, String item_name) {
        this.item_id = item_id;
        this.item_name = item_name;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getItem_name() {
        return item_name;
    }
}
