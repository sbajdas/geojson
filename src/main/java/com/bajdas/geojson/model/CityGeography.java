package com.bajdas.geojson.model;

import org.geojson.GeoJsonObject;
import org.geojson.GeometryCollection;

import java.util.List;

public class CityGeography {
    private final CityMetaData metaData;
    private GeometryCollection cityBoundaries;

    public CityGeography(CityMetaData metaData) {
        this.metaData = metaData;
    }

    public String getOsmId() {
        return this.metaData.getOsm_id();
    }

    public void addBoundaries(GeometryCollection cityBoundaries) {
        this.cityBoundaries = cityBoundaries;
    }

    public GeometryCollection getCityBoundaries(){
        return cityBoundaries;
    }

    List<GeoJsonObject> getCityGeometries(){
        return cityBoundaries.getGeometries();
    }
}
