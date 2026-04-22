package com.pathumi.smartcampusapi.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

import com.pathumi.smartcampusapi.models.Room;
import com.pathumi.smartcampusapi.models.Sensor;
import com.pathumi.smartcampusapi.models.SensorReading;

@Path("/summary")
@Produces(MediaType.APPLICATION_JSON)
public class SummaryResource {

    @GET
    public Response getFullSummary() {

        //use LinkedHashMap to preserve order
        Map<String, Object> summary = new LinkedHashMap<>();

        int totalRooms = RoomResource.rooms.size();
        int totalSensors = SensorResource.sensors.size();
        int totalReadings = 0;

        int active = 0;
        int maintenance = 0;

        Map<String, Integer> sensorTypes = new LinkedHashMap<>();

        //sensor stats
        for (Sensor sensor : SensorResource.sensors.values()) {

            if ("ACTIVE".equalsIgnoreCase(sensor.getStatus())) {
                active++;
            } else if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
                maintenance++;
            }

            String type = sensor.getType();
            sensorTypes.put(type, sensorTypes.getOrDefault(type, 0) + 1);
        }

        //build full room strucuture
        List<Object> roomList = new ArrayList<>();

        for (Room room : RoomResource.rooms.values()) {

            Map<String, Object> roomMap = new LinkedHashMap<>();
            roomMap.put("id", room.getId());
            roomMap.put("name", room.getName());
            roomMap.put("capacity", room.getCapacity());

            List<Object> sensorList = new ArrayList<>();

            for (String sensorId : room.getSensorIds()) {

                Sensor sensor = SensorResource.sensors.get(sensorId);

                if (sensor != null) {

                    Map<String, Object> sensorMap = new LinkedHashMap<>();
                    sensorMap.put("id", sensor.getId());
                    sensorMap.put("type", sensor.getType());
                    sensorMap.put("status", sensor.getStatus());
                    sensorMap.put("currentValue", sensor.getCurrentValue());

                    List<SensorReading> readings =
                        SensorReadingResource.readings.getOrDefault(sensorId, new ArrayList<>());

                    //count readings
                    totalReadings += readings.size();

                    sensorMap.put("readings", readings);

                    sensorList.add(sensorMap);
                }
            }

            roomMap.put("sensors", sensorList);
            roomList.add(roomMap);
        }

        //final ordered response
        summary.put("totalRooms", totalRooms);
        summary.put("totalSensors", totalSensors);
        summary.put("totalReadings", totalReadings);

        Map<String, Integer> statusMap = new LinkedHashMap<>();
        statusMap.put("ACTIVE", active);
        statusMap.put("MAINTENANCE", maintenance);

        summary.put("sensorStatus", statusMap);
        summary.put("sensorTypes", sensorTypes);

        summary.put("rooms", roomList);

        return Response.ok(summary).build();
    }
}