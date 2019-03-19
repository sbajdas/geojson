package com.bajdas.geojson.model;

import com.mapbox.geojson.Point;
import lombok.Builder;
import lombok.Data;
import org.geojson.GeometryCollection;

@Data
@Builder
public class NeighboursDTO {
    private boolean isNeigbouring;
    private double borderLength;
    private Point borderCenter;
    private GeometryCollection border;
}
