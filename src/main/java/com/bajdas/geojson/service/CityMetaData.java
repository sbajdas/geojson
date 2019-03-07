package com.bajdas.geojson.service;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class CityMetaData {
    String osm_type;
    String osm_id;
    String[] boundingbox;
    String lat;
    String lon;
    String display_name;
    String place_id;
}
