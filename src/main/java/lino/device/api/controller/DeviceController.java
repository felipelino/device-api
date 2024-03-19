package lino.device.api.controller;

import lino.device.api.dto.DeviceRequest;
import lino.device.api.dto.DeviceResponse;
import lino.device.api.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DeviceController {

    private DeviceService deviceService;
    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping(path = "/api/device")
    public ResponseEntity<List<DeviceResponse>> getAllDevices() {
        List<DeviceResponse> deviceResponses = this.deviceService.getAllDevices();
        return ResponseEntity.ok(deviceResponses);
    }

    @GetMapping(path = "/api/device/{id}")
    public ResponseEntity<DeviceResponse> getDeviceById(@PathVariable(name = "id", required = true) Long id) {

        DeviceResponse deviceResponse = this.deviceService.getDeviceById(id);
        if(deviceResponse != null) {
            return ResponseEntity.ok(deviceResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/api/device")
    public ResponseEntity<DeviceResponse> createDevice(@RequestBody DeviceRequest deviceRequest) {
        return null;
    }

    @PutMapping(path = "/api/device/{id}")
    public ResponseEntity<DeviceResponse> replaceDevice(@PathVariable(name = "id", required = true) Long id) {
        return null;
    }

    @PatchMapping(path = "/api/device/{id}")
    public ResponseEntity<DeviceResponse> updateDevice(@PathVariable(name = "id", required = true) Long id) {
        return null;
    }

    @DeleteMapping(path = "/api/device/{id}")
    public ResponseEntity deleteDevice(@PathVariable(name = "id", required = true) Long id) {
        return null;
    }

}