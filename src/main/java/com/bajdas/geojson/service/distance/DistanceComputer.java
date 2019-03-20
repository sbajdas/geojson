package com.bajdas.geojson.service.distance;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DistanceComputer implements Callable<PointsToMeasure> {
    private LinkedBlockingQueue<PointsToMeasure> queue;
    private PointsToMeasure farthestDistance = new PointsToMeasure(null,null);

    DistanceComputer(LinkedBlockingQueue<PointsToMeasure> queue) {
        this.queue = queue;
    }


    @Override
    public PointsToMeasure call() {
        while(!queue.isEmpty()) {
            try {
                PointsToMeasure point = queue.poll(10, TimeUnit.MILLISECONDS);
                if (point != null) {
                    double distance = point.calculateDistance();
                    if(distance > 0 && farthestDistance.getDistance() < distance) {
                        farthestDistance = point;
                        System.out.println(Thread.currentThread().getName() + ": Nowy dystans: " + distance);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + ": Obliczona linia: " + farthestDistance);
        return farthestDistance;
    }
}
