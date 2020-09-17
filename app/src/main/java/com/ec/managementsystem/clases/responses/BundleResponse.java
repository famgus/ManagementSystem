package com.ec.managementsystem.clases.responses;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BundleResponse implements Serializable {
    private Map<String, Integer> mapCodes;

    public BundleResponse(){
        mapCodes = new HashMap<>();
    }

    public Map<String, Integer> getMapCodes() {
        return mapCodes;
    }

    public void setMapCodes(Map<String, Integer> mapCodes) {
        this.mapCodes = mapCodes;
    }
}
