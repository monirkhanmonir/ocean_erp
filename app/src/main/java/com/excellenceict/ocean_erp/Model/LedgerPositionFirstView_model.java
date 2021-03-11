package com.excellenceict.ocean_erp.Model;

import java.io.Serializable;

public class LedgerPositionFirstView_model implements Serializable {
    String headCode;

    public LedgerPositionFirstView_model(String headCode) {
        this.headCode = headCode;
    }

    public String getHeadCode() {
        return headCode;
    }

    public void setHeadCode(String headCode) {
        this.headCode = headCode;
    }


    @Override
    public String toString() {
        return  headCode ;
    }
}
