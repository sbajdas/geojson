package com.bajdas.geojson.service.distance;

import com.mapbox.geojson.Point;
import com.mapbox.turf.TurfMeasurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.logging.log4j.util.PropertySource;

@Data
class PointsToMeasure implements Comparable {
    private Point from;
    private Point to;
    private double distance = 0.0d;

    PointsToMeasure(Point from, Point to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public int compareTo(Object o) {
        double result = this.distance - ((PointsToMeasure) o).distance;
        if(result > 0)
            return 1;
        else if(result < 0)
            return  -1;
        else
            return 0;
    }
    double calculateDistance() {
        distance = TurfMeasurement.distance(from, to);
        return distance;
    }

    @Override
    public String toString() {
        return "PointsToMeasure{" +
                "from=" + from +
                ", to=" + to +
                ", distance=" + distance +
                '}';
    }
}
