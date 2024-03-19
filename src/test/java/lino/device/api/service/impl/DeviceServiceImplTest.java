package lino.device.api.service.impl;


import lino.device.api.dto.DeviceResponse;
import lino.device.api.repository.DeviceRepository;
import lino.device.api.repository.model.Device;
import lino.device.api.service.DeviceService;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeviceServiceImplTest {

    private DeviceRepository deviceRepositoryMock = mock(DeviceRepository.class);
    private DeviceService deviceService = new DeviceServiceImpl(deviceRepositoryMock);

    @Test
    public void toDeviceResponse_success() {
        // Prepare
        Device device1 = new Device();
        device1.setId(1l);
        device1.setName("MyCustomName");
        device1.setBrand("MyCustomBrand");
        device1.setCreationTime(new Date().getTime());

        // Execute
        DeviceResponse deviceResponse = DeviceServiceImpl.toDeviceResponse(device1);

        // Assert
        Assert.assertNotNull(deviceResponse);
        Assert.assertEquals(device1.getName(), deviceResponse.getName());
        Assert.assertEquals(device1.getBrand(), deviceResponse.getBrand());
        Assert.assertEquals(device1.getId(), deviceResponse.getId());
        Assert.assertEquals(device1.getCreationTime(), deviceResponse.getCreationTime());

    }

    @Test
    public void toDeviceResponse_whenDeviceIsNull() {

        // Execute
        DeviceResponse deviceResponse = DeviceServiceImpl.toDeviceResponse(null);

        // Assert
        Assert.assertNull(deviceResponse);
    }

    @Test
    public void getAllDevices_whenDbNotEmpty() throws Exception {

        // Prepare
        Device device1 = new Device();
        device1.setId(1l);
        device1.setName("MyCustomName");
        device1.setBrand("MyCustomBrand");
        device1.setCreationTime(new Date().getTime());
        List<Device> deviceList = Arrays.asList(device1);
        when(this.deviceRepositoryMock.findAll()).thenReturn(deviceList);

        // Execute
        List<DeviceResponse> deviceResponseList = this.deviceService.getAllDevices();

        // Assert
        Assert.assertNotNull(deviceResponseList);
        Assert.assertEquals(1, deviceResponseList.size());
        DeviceResponse deviceResponse = deviceResponseList.get(0);
        Assert.assertNotNull(deviceResponse);
        Assert.assertEquals(device1.getName(), deviceResponse.getName());
        Assert.assertEquals(device1.getBrand(), deviceResponse.getBrand());
        Assert.assertEquals(device1.getId(), deviceResponse.getId());
        Assert.assertEquals(device1.getCreationTime(), deviceResponse.getCreationTime());
    }

    @Test
    public void getAllDevices_whenDbIsEmpty() throws Exception {

        // Prepare
        when(this.deviceRepositoryMock.findAll()).thenReturn(new ArrayList<>());

        // Execute
        List<DeviceResponse> deviceResponseList = this.deviceService.getAllDevices();

        // Assert
        Assert.assertNotNull(deviceResponseList);
        Assert.assertEquals(0, deviceResponseList.size());
    }

    @Test
    public void findDeviceById_whenNotFound() throws Exception {

        // Prepare
        Optional<Device> optional = Optional.ofNullable(null);
        when(this.deviceRepositoryMock.findById(anyLong())).thenReturn(optional);

        // Execute
        DeviceResponse deviceResponse = this.deviceService.getDeviceById(99l);

        // Assert
        Assert.assertNull(deviceResponse);
    }

    @Test
    public void findDeviceById_whenFound() throws Exception {

        // Prepare
        Optional<Device> optional = Optional.ofNullable(new Device());
        when(this.deviceRepositoryMock.findById(anyLong())).thenReturn(optional);

        // Execute
        DeviceResponse deviceResponse = this.deviceService.getDeviceById(99l);

        // Assert
        Assert.assertNotNull(deviceResponse);
    }

}