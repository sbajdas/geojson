package com.bajdas.geojson.service;

import com.mapbox.geojson.Point;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DistancePoint {
    private Point from;
    private Point to;
    private double distance;
}
