package com.ec.managementsystem.clases;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Vendedores implements Serializable {
    @SerializedName("Tipousuario")
    private Integer tipousuario;
    @SerializedName("Nomvendedor")
    private String nomvendedor;
    @SerializedName("Codvendedor")
    private Integer codvendedor;
    private boolean isSelected;

    public Vendedores() {
        this.isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Integer getTipousuario() {
        return tipousuario;
    }

    public void setTipousuario(Integer tipousuario) {
        this.tipousuario = tipousuario;
    }

    public String getNomvendedor() {
        return nomvendedor;
    }

    public void setNomvendedor(String nomvendedor) {
        this.nomvendedor = nomvendedor;
    }

    public Integer getCodvendedor() {
        return codvendedor;
    }

    public void setCodvendedor(Integer codvendedor) {
        this.codvendedor = codvendedor;
    }
}
