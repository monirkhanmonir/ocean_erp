package com.excellenceict.ocean_erp;

import java.io.Serializable;

public class EmpInfo_Entity implements Serializable {
    String persong_no;
    String F_name;
    String L_name;
    String Dept_name;
    String Desig_name;
    String Phone_mobile;
    String Email_office;

    public EmpInfo_Entity(String persong_no, String f_name, String l_name, String dept_name, String desig_name, String phone_mobile, String email_office) {
        this.persong_no = persong_no;
        this.F_name = f_name;
        this.L_name = l_name;
        this.Dept_name = dept_name;
        this.Desig_name = desig_name;
        this.Phone_mobile = phone_mobile;
        this.Email_office = email_office;


    }

    public String getPersong_no() {
        return persong_no;
    }

    public String getF_name() {
        return F_name;
    }

    public String getL_name() {
        return L_name;
    }

    public String getDept_name() {
        return Dept_name;
    }

    public String getDesig_name() {
        return Desig_name;
    }

    public String getPhone_mobile() {
        return Phone_mobile;
    }

    public String getEmail_office() {
        return Email_office;
    }


}
