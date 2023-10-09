/**
 Copyright 2023, Ralf Ulrich, ralf.m.ulrich@gmail.com

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package xyz.wirklich.duomo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import xyz.wirklich.astro.sun.SolarLocation;
import xyz.wirklich.astro.time.FractionOfDay;
import xyz.wirklich.astro.time.JulianDay;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class DuomoController {


    private double milanoLong = 9.188540;
    private double milanoLat = 45.464664;

    @RequestMapping(value = "/times", method = RequestMethod.POST, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<DuomoResponse> getDuomoInfo(@RequestBody DuomoRequest input) {

        ZonedDateTime now = timeInMilaon();
        ZonedDateTime inputDate = ZonedDateTime.of(input.getYear(), input.getMonth(), input.getDay(), 0, 0, 0, 0, ZoneId.of("UTC"));

        List<DuomoResponse> results = new ArrayList<>();

        int offsetDays = 5;
        for (int i = -offsetDays; i <= offsetDays; ++i) {
            ZonedDateTime date = inputDate.plusDays(i);
            // xyz.wirklich.astro
            SolarLocation solarLocation = new SolarLocation(milanoLat, milanoLong, date);
            double v = solarLocation.solarNoon();
            FractionOfDay fod = new FractionOfDay(v);
            ZonedDateTime noonDate = ZonedDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), fod.getHour(), fod.getMinute(), fod.getSecond(), fod.getNanoSecond(), ZoneId.of("UTC"));
            DuomoResponse time = new DuomoResponse(noonDate, solarLocation.solarZenithAngle(), i);
            results.add(time);
        }

        return results;
    }

    private ZonedDateTime timeInMilaon() {

        ZoneId zoneMontréal = ZoneId.of("Europe/Rome");
        ZonedDateTime nowMilano = ZonedDateTime.now(zoneMontréal);
        return nowMilano;
    }

    private double SolarNoon() {

        JulianDay jd = new JulianDay(timeInMilaon());
        SolarLocation sl = new SolarLocation(milanoLat, milanoLong, jd);
        return sl.solarNoon();
    }

    public static class DuomoResponse {

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Europe/Milano")
        @JsonSerialize(using = ZonedDateTimeSerializer.class)
        final private ZonedDateTime zenithTimes;
        final private Double zenithAngles;
        final private Integer distanceInTime;

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

    public static class DuomoRequest {
        private double lat;
        private double lon;
        private int year;
        private int month;
        private int day;

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        // getters and setters omitted
    }
}

