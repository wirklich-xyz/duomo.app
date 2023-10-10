package xyz.wirklich.duomo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

import java.time.ZonedDateTime;

public class DuomoResponse {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Europe/Milano")
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    private ZonedDateTime zenithTimes;
    private Double zenithAngles;

    public void setZenithTimes(ZonedDateTime zenithTimes) {
        this.zenithTimes = zenithTimes;
    }

    public void setZenithAngles(Double zenithAngles) {
        this.zenithAngles = zenithAngles;
    }

    public void setDistanceInTime(Integer distanceInTime) {
        this.distanceInTime = distanceInTime;
    }

    private Integer distanceInTime;

    public DuomoResponse(ZonedDateTime zenithTimes, Double zenithAngles, Integer distanceInTime) {
        this.zenithTimes = zenithTimes;
        this.zenithAngles = zenithAngles;
        this.distanceInTime = distanceInTime;
    }

    public ZonedDateTime getZenithTimes() {
        return zenithTimes;
    }

    public Double getZenithAngles() {
        return zenithAngles;
    }

    public Integer getDistanceInTime() {
        return distanceInTime;
    }
}
