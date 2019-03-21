package com.bajdas.geojson.model;

import com.mapbox.geojson.Point;
import lombok.Data;
import org.geojson.GeoJsonObject;
import org.geojson.GeometryCollection;
import org.geojson.LineString;

import java.util.ArrayList;
import java.util.List;

@Data
public class CityGeography {
    private final CityMetaData metaData;
    private GeometryCollection cityBoundaries;
    private LineString longestLine;
    private List<Point> pointList = new ArrayList<>();

    public CityGeography(CityMetaData metaData) {
        this.metaData = metaData;
    }

    public String getOsmId() {
        return this.metaData.getOsm_id();
    }

    public List<GeoJsonObject> getCityGeometries() {
        return cityBoundaries.getGeometries();
    }
}
