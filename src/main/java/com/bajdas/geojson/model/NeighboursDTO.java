package com.bajdas.geojson.model;

import lombok.Builder;
import lombok.Data;
import org.geojson.GeometryCollection;

@Data
@Builder
public class NeighboursDTO {
    private boolean isNeigbouring;
    private double borderLength;
    private GeometryCollection borderWithCenterPoint;
}
