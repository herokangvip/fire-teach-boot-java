package com.example.demo.domain.mongo;

import lombok.Data;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;

import java.io.Serializable;

@Data
public class GeoInfo implements Serializable {
    private String name;
    private double[] loc;

    public GeoInfo() {
    }

    public GeoInfo(String name, Point point) {
        this.name = name;
        loc = new double[]{point.getX(), point.getY()};
    }
}
