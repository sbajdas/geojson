package com.bajdas.geojson.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityMetaData {
    private String osm_type;
    private String osm_id;
    private String[] boundingbox;
    private String lat;
    private String lon;
    private String display_name;
    private String place_id;
}
