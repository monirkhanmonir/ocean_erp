package com.excellenceict.ocean_erp;

public class CurrentStock_Manufacturer_Entity_A {
    String manufacture_Id;
    String menufacture_Name;

    public CurrentStock_Manufacturer_Entity_A(String manufacture_Id, String menufacture_Name) {
        this.manufacture_Id = manufacture_Id;
        this.menufacture_Name = menufacture_Name;
    }

    public String getManufacture_Id() {
        return manufacture_Id;
    }

    public String getMenufacture_Name() {
        return menufacture_Name;
    }

    @Override
    public String toString() {
        return menufacture_Name;
    }
}
