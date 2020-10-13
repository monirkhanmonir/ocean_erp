package com.ocean.orcl;

public class Acc_PartyDueStatement_Result_Entity {
    String ud_no;
    String sub_header_name;
    String analyzer;
    String roche;
    String sysmex;

    public Acc_PartyDueStatement_Result_Entity(String ud_no, String sub_header_name, String analyzer, String roche, String sysmex) {
        this.ud_no = ud_no;
        this.sub_header_name = sub_header_name;
        this.analyzer = analyzer;
        this.roche = roche;
        this.sysmex = sysmex;
    }

    public String getUd_no() {
        return ud_no;
    }

    public String getSub_header_name() {
        return sub_header_name;
    }

    public String getAnalyzer() {
        return analyzer;
    }

    public String getRoche() {
        return roche;
    }

    public String getSysmex() {
        return sysmex;
    }
}
