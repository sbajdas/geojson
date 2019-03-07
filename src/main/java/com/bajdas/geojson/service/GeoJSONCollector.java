package com.bajdas.geojson.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.geojson.GeoJsonObject;

import java.util.List;

@Data
@NoArgsConstructor
public class GeoJSONCollector {
    List<GeoJsonObject> geometries;

}
