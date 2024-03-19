package lino.device.api.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lino.device.api.dto.DeviceRequest;
import lino.device.api.dto.DeviceResponse;
import lino.device.api.repository.DeviceRepository;
import lino.device.api.repository.model.Device;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeviceRepository deviceRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private DeviceRequest defaultPostDeviceRequest = null;
    private List<Device> defaultDevicesInDatabase = null;

    @Before
    public void setup() throws Exception {
        this.deviceRepository.deleteAll();
        Device device1 = new Device();
        device1.setName("MyCustomName");
        device1.setBrand("MyCustomBrand");
        device1.setCreationTime(new Date().getTime());

        Device device2 = new Device();
        device2.setName("OtherName");
        device2.setBrand("OtherBrand");
        device2.setCreationTime(new Date().getTime());
        this.defaultDevicesInDatabase = Arrays.asList(device1, device2);

        this.deviceRepository.saveAll(this.defaultDevicesInDatabase);
    }

    @Test
    public void getAllDevices_databaseNotEmpty() throws Exception {

        // Execute and Assert
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/device"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        Assert.assertNotNull(json);

        List<DeviceResponse> deviceList = this.objectMapper.readValue(json, new TypeReference<List<DeviceResponse>>(){});
        Assert.assertEquals(2, deviceList.size());
        for(Device device : this.defaultDevicesInDatabase) {
            Assert.assertEquals(1, deviceList.stream().filter(deviceResponse -> deviceResponse.getName().equals(device.getName())).count());
        }
    }

    @Test
    public void getAllDevices_databaseIsEmpty() throws Exception {
        // Prepare
        this.deviceRepository.deleteAll();

        // Execute and Assert
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/device"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        Assert.assertNotNull(json);

        List<DeviceResponse> deviceList = this.objectMapper.readValue(json, new TypeReference<List<DeviceResponse>>(){});
        Assert.assertEquals(0, deviceList.size());
    }

    @Test
    public void getDeviceById_whenFound() throws Exception {

        Device device = new Device();
        device.setName("device name");
        device.setBrand("device brand");
        device.setCreationTime(new Date().getTime());
        device = this.deviceRepository.save(device);

        // Execute and Assert
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/device/"+device.getId()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        Assert.assertNotNull(json);

        DeviceResponse deviceResponse = this.objectMapper.readValue(json, DeviceResponse.class);
        Assert.assertNotNull(deviceResponse);
        Assert.assertEquals(device.getId(), deviceResponse.getId());
        Assert.assertEquals(device.getBrand(), deviceResponse.getBrand());
        Assert.assertEquals(device.getName(), deviceResponse.getName());
        Assert.assertEquals(device.getCreationTime(), deviceResponse.getCreationTime());
    }

    @Test
    public void createDevice_success() throws Exception {
        // Prepare
        DeviceRequest deviceRequest = new DeviceRequest();
        deviceRequest.setName("DeviceName");
        deviceRequest.setBrand("DeviceBrand");

        String jsonRequest = this.objectMapper.writeValueAsString(deviceRequest);

        // Execute and Assert
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/device")
                        .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        Assert.assertNotNull(json);
        DeviceResponse deviceResponse = this.objectMapper.readValue(json, DeviceResponse.class);
        Assert.assertNotNull(deviceResponse.getId());
        Assert.assertNotNull(deviceResponse.getCreationTime());
        Assert.assertEquals(deviceRequest.getName(), deviceResponse.getName());
        Assert.assertEquals(deviceRequest.getBrand(), deviceResponse.getBrand());
    }
}
