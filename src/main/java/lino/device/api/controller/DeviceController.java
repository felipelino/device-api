package lino.device.api.controller;

import lino.device.api.dto.DeviceRequest;
import lino.device.api.dto.DeviceResponse;
import lino.device.api.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
        DeviceResponse deviceResponse = this.deviceService.saveDevice(deviceRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(deviceResponse.getId()).toUri();
        return ResponseEntity.created(location).body(deviceResponse);
    }

    @PutMapping(path = "/api/device/{id}")
    public ResponseEntity<DeviceResponse> replaceDevice(@PathVariable(name = "id", required = true) Long id, @RequestBody DeviceRequest deviceRequest) {
        DeviceResponse deviceResponse = this.deviceService.replaceDevice(id, deviceRequest);
        if(deviceResponse == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(deviceResponse);
    }

    @PostMapping(path = "/api/device/{id}")
    public ResponseEntity<DeviceResponse> updateDevice(@PathVariable(name = "id", required = true) Long id, @RequestBody DeviceRequest deviceRequest) {
        DeviceResponse deviceResponse = this.deviceService.updateDevice(id, deviceRequest);
        if(deviceResponse == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(deviceResponse);
    }

    @DeleteMapping(path = "/api/device/{id}")
    public ResponseEntity deleteDevice(@PathVariable(name = "id", required = true) Long id) {
        this.deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }

}
