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

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import xyz.wirklich.astro.sun.SolarLocation;
import xyz.wirklich.astro.time.FractionOfDay;
import xyz.wirklich.astro.time.JulianDay;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@EnableWebMvc
public class DuomoController {

    private double milanoLong = 9.188540;
    private double milanoLat = 45.464664;

    @RequestMapping(value = "/times", method = RequestMethod.POST, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<DuomoResponse> getDuomoInfo(@RequestBody DuomoRequest input) {

        System.out.println("request: " + input.getYear() + " " + input.getMonth() + " " + input.getDay());
        ZonedDateTime now = timeInMilaon();
        System.out.println("now: " + now);
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

}

