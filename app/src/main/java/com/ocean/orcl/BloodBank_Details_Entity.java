package com.ocean.orcl;

public class BloodBank_Details_Entity {
    String person_id;
    String emp_name;
    String desig_dept;
    String reporters_to;
    String phone_mobile;
    String phone_home;
    String email_personal;
    String email_official;
    String address1;
    String join_date;
    String pabx;
    String blood_grp;

    public BloodBank_Details_Entity(String person_id, String emp_name, String desig_dept, String reporters_to, String phone_mobile, String phone_home, String email_personal, String email_official, String address1, String join_date, String pabx, String blood_grp) {
        this.person_id = person_id;
        this.emp_name = emp_name;
        this.desig_dept = desig_dept;
        this.reporters_to = reporters_to;
        this.phone_mobile = phone_mobile;
        this.phone_home = phone_home;
        this.email_personal = email_personal;
        this.email_official = email_official;
        this.address1 = address1;
        this.join_date = join_date;
        this.pabx = pabx;
        this.blood_grp = blood_grp;
    }

    public String getPerson_id() {
        return person_id;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public String getDesig_dept() {
        return desig_dept;
    }

    public String getReporters_to() {
        return reporters_to;
    }

    public String getPhone_mobile() {
        return phone_mobile;
    }

    public String getPhone_home() {
        return phone_home;
    }

    public String getEmail_personal() {
        return email_personal;
    }

    public String getEmail_official() {
        return email_official;
    }

    public String getAddress1() {
        return address1;
    }

    public String getJoin_date() {
        return join_date;
    }

    public String getPabx() {
        return pabx;
    }

    public String getBlood_grp() {
        return blood_grp;
    }
}
