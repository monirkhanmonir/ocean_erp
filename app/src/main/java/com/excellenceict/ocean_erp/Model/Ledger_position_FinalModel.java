package com.excellenceict.ocean_erp.Model;

import java.io.Serializable;

public class Ledger_position_FinalModel implements Serializable {
    String head;
    String subHead;
    String balance;
    String type;

    public Ledger_position_FinalModel(String head, String subHead, String balance,String type) {
        this.head = head;
        this.subHead = subHead;
        this.balance = balance;
        this.type = type;
    }

    public String getHead() {
        return head;
    }

    public String getSubHead() {
        return subHead;
    }

    public String getBalance() {
        return balance;
    }

    public String getType() {
        return type;
    }
}
