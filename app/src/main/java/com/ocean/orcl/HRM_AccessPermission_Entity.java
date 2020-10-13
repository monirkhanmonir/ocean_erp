package com.ocean.orcl;

public class HRM_AccessPermission_Entity {
    String posation_id;
    String title_name;
    String access;

    public HRM_AccessPermission_Entity(String posation_id, String title_name, String access) {
        this.posation_id = posation_id;
        this.title_name = title_name;
        this.access = access;
    }

    public String getPosation_id() {
        return posation_id;
    }

    public String getTitle_name() {
        return title_name;
    }

    public String getAccess() {
        return access;
    }
}
