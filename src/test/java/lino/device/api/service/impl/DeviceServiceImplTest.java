package lino.device.api.service.impl;


import lino.device.api.dto.DeviceRequest;
import lino.device.api.dto.DeviceResponse;
import lino.device.api.repository.DeviceRepository;
import lino.device.api.repository.model.Device;
import lino.device.api.service.DeviceService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
    public void toDevice_success() {
        // Prepare
        DeviceRequest deviceRequest = new DeviceRequest();
        deviceRequest.setName("MyCustomName");
        deviceRequest.setBrand("MyCustomBrand");

        // Execute
        Device device = DeviceServiceImpl.toDevice(deviceRequest);

        // Assert
        Assert.assertNotNull(device);
        Assert.assertEquals(deviceRequest.getName(), device.getName());
        Assert.assertEquals(deviceRequest.getBrand(), device.getBrand());
        Assert.assertNull(device.getId());
        Assert.assertNotNull(device.getCreationTime());
    }

    @Test
    public void toDevice_whenInputIsNull() {
        // Prepare

        // Execute
        Device device = DeviceServiceImpl.toDevice(null);

        // Assert
        Assert.assertNull(device);
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
        verify(this.deviceRepositoryMock, Mockito.times(1)).findAll();
        verifyNoMoreInteractions(this.deviceRepositoryMock);
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
        verify(this.deviceRepositoryMock, Mockito.times(1)).findAll();
        verifyNoMoreInteractions(this.deviceRepositoryMock);
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
        verify(this.deviceRepositoryMock, Mockito.times(1)).findById(eq(99l));
        verifyNoMoreInteractions(this.deviceRepositoryMock);
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
        verify(this.deviceRepositoryMock, Mockito.times(1)).findById(eq(99l));
        verifyNoMoreInteractions(this.deviceRepositoryMock);
    }

    @Test
    public void createDevice_success() throws Exception {
        // Prepare
        Device device1 = new Device();
        device1.setId(1l);
        device1.setName("MyCustomName");
        device1.setBrand("MyCustomBrand");
        device1.setCreationTime(new Date().getTime());
        when(this.deviceRepositoryMock.save(any(Device.class))).thenReturn(device1);

        // Execute
        DeviceResponse deviceResponse = this.deviceService.saveDevice(new DeviceRequest());

        // Assert
        Assert.assertNotNull(deviceResponse);
        Assert.assertEquals(device1.getName(), deviceResponse.getName());
        Assert.assertEquals(device1.getBrand(), deviceResponse.getBrand());
        Assert.assertEquals(device1.getId(), deviceResponse.getId());
        Assert.assertEquals(device1.getCreationTime(), deviceResponse.getCreationTime());
        verify(this.deviceRepositoryMock, Mockito.times(1)).save(any(Device.class));
        verifyNoMoreInteractions(this.deviceRepositoryMock);
    }

    @Test
    public void replaceDevice_success() throws Exception {

        // Prepare
        Device device1 = new Device();
        device1.setId(1l);
        device1.setName("MyCustomName");
        device1.setBrand("MyCustomBrand");
        device1.setCreationTime(1710849243328l); // hard coded date - should not be updated 2023-03-19 11:54
        when((this.deviceRepositoryMock.findById(anyLong()))).thenReturn(Optional.of(device1));
        when(this.deviceRepositoryMock.save(any(Device.class))).thenReturn(device1);

        DeviceResponse deviceResponse = this.deviceService.replaceDevice(device1.getId(), new DeviceRequest());

        // Assert
        Assert.assertNotNull(deviceResponse);
        Assert.assertEquals(device1.getName(), deviceResponse.getName());
        Assert.assertEquals(device1.getBrand(), deviceResponse.getBrand());
        Assert.assertEquals(device1.getId(), deviceResponse.getId());
        Assert.assertEquals(device1.getCreationTime(), deviceResponse.getCreationTime());
        verify(this.deviceRepositoryMock, Mockito.times(1)).findById(eq(device1.getId()));
        verify(this.deviceRepositoryMock, Mockito.times(1)).save(any(Device.class));
        verifyNoMoreInteractions(this.deviceRepositoryMock);
    }

    @Test
    public void updateDevice_success() throws Exception {

        // Prepare
        Device device1 = new Device();
        device1.setId(1l);
        device1.setName("MyCustomName");
        device1.setBrand("MyCustomBrand");
        device1.setCreationTime(1710849243328l); // hard coded date - should not be updated 2023-03-19 11:54
        when((this.deviceRepositoryMock.findById(anyLong()))).thenReturn(Optional.of(device1));
        when(this.deviceRepositoryMock.save(any(Device.class))).thenReturn(device1);

        DeviceResponse deviceResponse = this.deviceService.updateDevice(device1.getId(), new DeviceRequest());

        // Assert
        Assert.assertNotNull(deviceResponse);
        Assert.assertEquals(device1.getName(), deviceResponse.getName());
        Assert.assertEquals(device1.getBrand(), deviceResponse.getBrand());
        Assert.assertEquals(device1.getId(), deviceResponse.getId());
        Assert.assertEquals(device1.getCreationTime(), deviceResponse.getCreationTime());
        verify(this.deviceRepositoryMock, Mockito.times(1)).findById(eq(device1.getId()));
        verify(this.deviceRepositoryMock, Mockito.times(1)).save(any(Device.class));
        verifyNoMoreInteractions(this.deviceRepositoryMock);
    }

    @Test
    public void deleteDevice_success() throws Exception {

        // Execute
        this.deviceService.deleteDevice(99l);

        // Assert
        verify(this.deviceRepositoryMock, Mockito.times(1)).deleteById(eq(99l));
        verifyNoMoreInteractions(this.deviceRepositoryMock);
    }
}
