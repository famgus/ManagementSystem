package com.ec.managementsystem.clases.request;

import java.io.Serializable;

public class QualityControlRequest implements Serializable {
    private String barCode;
    private String codeLocation;
    private Integer motive;
    private String descriptionMotive;
    private String user;


    public QualityControlRequest() {
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getCodeLocation() {
        return codeLocation;
    }

    public void setCodeLocation(String codeLocation) {
        this.codeLocation = codeLocation;
    }

    public Integer getMotive() {
        return motive;
    }

    public void setMotive(Integer motive) {
        this.motive = motive;
    }

    public String getDescriptionMotive() {
        return descriptionMotive;
    }

    public void setDescriptionMotive(String descriptionMotive) {
        this.descriptionMotive = descriptionMotive;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
