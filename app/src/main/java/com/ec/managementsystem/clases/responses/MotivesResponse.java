package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MotivesResponse implements Serializable {
    @SerializedName("idMotive")
    private Integer idMotive;
    @SerializedName("description")
    private String description;
    @SerializedName("observation")
    private String observation;

    public  MotivesResponse(){}


    public Integer getIdMotive() {
        return idMotive;
    }

    public void setIdMotive(Integer idMotive) {
        this.idMotive = idMotive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
