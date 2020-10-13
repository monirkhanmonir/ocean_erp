package com.ocean.orcl;

public class ACC_partyDueStatement_Entity {
    String sub_head_name;
    String sub_head_code;

    public ACC_partyDueStatement_Entity(String sub_head_name, String sub_head_code) {
        this.sub_head_name = sub_head_name;
        this.sub_head_code = sub_head_code;
    }

    public String getSub_head_name() {
        return sub_head_name;
    }

    public String getSub_head_code() {
        return sub_head_code;
    }
}
