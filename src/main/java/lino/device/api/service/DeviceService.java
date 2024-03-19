package lino.device.api.service;

import lino.device.api.dto.DeviceRequest;
import lino.device.api.dto.DeviceResponse;

import java.util.List;

public interface DeviceService
{
    List<DeviceResponse> getAllDevices();

    DeviceResponse getDeviceById(Long id);

    DeviceResponse createDevice(DeviceRequest deviceRequest);
}
