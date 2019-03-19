package com.bajdas.geojson.model;

import com.mapbox.geojson.Point;
import lombok.Data;
import org.geojson.GeometryCollection;

@Data
public class NeighboursDTO {
    private boolean isNeigboring;
    private double borderLength;
    private Point borderCenter;
    private GeometryCollection border;
}
