package com.ec.managementsystem.clases.request;

public class BoxMasterRequest {
    private int actionPath;
    private String barCodeArticle;
    private String barCodeBoxMasterOrigin;
    private String barCodeBoxMasterDestiny;
    private Integer quantityArticle;
    private String codeStorage;

    public BoxMasterRequest() {
    }

    public String getBarCodeArticle() {
        return barCodeArticle;
    }

    public void setBarCodeArticle(String barCodeArticle) {
        this.barCodeArticle = barCodeArticle;
    }

    public String getBarCodeBoxMasterOrigin() {
        return barCodeBoxMasterOrigin;
    }

    public void setBarCodeBoxMasterOrigin(String barCodeBoxMasterOrigin) {
        this.barCodeBoxMasterOrigin = barCodeBoxMasterOrigin;
    }

    public String getBarCodeBoxMasterDestiny() {
        return barCodeBoxMasterDestiny;
    }

    public void setBarCodeBoxMasterDestiny(String barCodeBoxMasterDestiny) {
        this.barCodeBoxMasterDestiny = barCodeBoxMasterDestiny;
    }

    public Integer getQuantityArticle() {
        return quantityArticle;
    }

    public void setQuantityArticle(Integer quantityArticle) {
        this.quantityArticle = quantityArticle;
    }

    public String getCodeStorage() {
        return codeStorage;
    }

    public void setCodeStorage(String codeStorage) {
        this.codeStorage = codeStorage;
    }

    public int getActionPath() {
        return actionPath;
    }

    public void setActionPath(int actionPath) {
        this.actionPath = actionPath;
    }
}
