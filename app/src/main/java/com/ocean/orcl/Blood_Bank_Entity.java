package com.ocean.orcl;

public class Blood_Bank_Entity {
    String F_name;
    String L_name;
    String Dept_name;
    String Desig_name;
    String Phone_mobile;
    String Blood_Grp;

    public Blood_Bank_Entity(String f_name, String l_name, String dept_name, String desig_name, String phone_mobile, String blood_Grp) {
        F_name = f_name;
        L_name = l_name;
        Dept_name = dept_name;
        Desig_name = desig_name;
        Phone_mobile = phone_mobile;
        Blood_Grp = blood_Grp;
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

    public String getBlood_Grp() {
        return Blood_Grp;
    }
}
