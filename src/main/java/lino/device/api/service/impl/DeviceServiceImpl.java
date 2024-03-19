package lino.device.api.service.impl;

import lino.device.api.dto.DeviceResponse;
import lino.device.api.repository.model.Device;
import lino.device.api.service.DeviceService;
import lino.device.api.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {

    private DeviceRepository deviceRepository;

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public List<DeviceResponse> getAllDevices() {
       Iterable<Device> devices = this.deviceRepository.findAll();
       List<DeviceResponse> deviceResponseList = new ArrayList<>();
       if(devices != null) {
           for(Device device : devices) {
               deviceResponseList.add(toDeviceResponse(device));
           }
       }
       return deviceResponseList;
    }

    @Override
    public DeviceResponse getDeviceById(Long id) {
        Optional<Device> optional = this.deviceRepository.findById(id);
        if(optional.isPresent()) {
            return toDeviceResponse(optional.get());
        }
        return null;
    }

    protected static DeviceResponse toDeviceResponse(Device device) {
        if(device == null) {
            return null;
        }
        DeviceResponse deviceResponse = new DeviceResponse();
        deviceResponse.setId(device.getId());
        deviceResponse.setCreationTime(device.getCreationTime());
        deviceResponse.setName(device.getName());
        deviceResponse.setBrand(device.getBrand());
        return deviceResponse;
    }
}