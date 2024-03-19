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
    public void getAllDevicesByBrand_databaseNotEmpty() throws Exception {

        // Execute and Assert
        Device device = defaultDevicesInDatabase.get(1);
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/device?brand="+device.getBrand()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        Assert.assertNotNull(json);

        List<DeviceResponse> deviceList = this.objectMapper.readValue(json, new TypeReference<List<DeviceResponse>>(){});
        Assert.assertEquals(1, deviceList.size());
        Assert.assertEquals(1, deviceList.stream().filter(deviceResponse -> deviceResponse.getName().equals(device.getName())).count());
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

    @Test
    public void replaceDevice_whenExists() throws Exception {

        Device device = new Device();
        device.setName("device name");
        device.setBrand("device brand");;
        device.setCreationTime(1710849243328l); // hard coded date - should not be updated 2023-03-19 11:54
        device = this.deviceRepository.save(device);

        DeviceRequest deviceRequest = new DeviceRequest();
        deviceRequest.setName("changed name");
        deviceRequest.setBrand("changed brand");

        String jsonRequest = this.objectMapper.writeValueAsString(deviceRequest);

        // Execute and Assert
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put("/api/device/"+device.getId())
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        Assert.assertNotNull(json);
        DeviceResponse deviceResponse = this.objectMapper.readValue(json, DeviceResponse.class);
        Assert.assertEquals(device.getId(), deviceResponse.getId());
        Assert.assertEquals(device.getCreationTime(), deviceResponse.getCreationTime());
        Assert.assertEquals(deviceRequest.getName(), deviceResponse.getName());
        Assert.assertEquals(deviceRequest.getBrand(), deviceResponse.getBrand());
    }

    @Test
    public void replaceDevice_whenNotExists() throws Exception {
        // Prepare
        DeviceRequest deviceRequest = new DeviceRequest();
        deviceRequest.setName("changed name");
        deviceRequest.setBrand("changed brand");

        String jsonRequest = this.objectMapper.writeValueAsString(deviceRequest);

        // Execute and Assert
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put("/api/device/99")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andReturn();
    }

    @Test
    public void updateDevice_whenNotExists() throws Exception {
        // Prepare
        DeviceRequest deviceRequest = new DeviceRequest();
        deviceRequest.setName("changed name");
        deviceRequest.setBrand("changed brand");

        String jsonRequest = this.objectMapper.writeValueAsString(deviceRequest);

        // Execute and Assert
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/device/99")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andReturn();
    }

    @Test
    public void updateDevice_whenExistsChangeName() throws Exception {
        // Prepare
        Device device = new Device();
        device.setName("device name");
        device.setBrand("device brand");
        device.setCreationTime(1710849243328l); // hard coded date - should not be updated 2023-03-19 11:54
        device = this.deviceRepository.save(device);

        DeviceRequest deviceRequest = new DeviceRequest();
        deviceRequest.setName("changed name");

        String jsonRequest = this.objectMapper.writeValueAsString(deviceRequest);

        // Execute and Assert
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/device/"+device.getId())
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        Assert.assertNotNull(json);
        DeviceResponse deviceResponse = this.objectMapper.readValue(json, DeviceResponse.class);
        Assert.assertEquals(device.getId(), deviceResponse.getId());
        Assert.assertEquals(device.getCreationTime(), deviceResponse.getCreationTime()); // not changed
        Assert.assertEquals(deviceRequest.getName(), deviceResponse.getName());
        Assert.assertEquals(device.getBrand(), deviceResponse.getBrand()); // Not changed
    }

    @Test
    public void updateDevice_whenExistsChangeBrand() throws Exception {
        // Prepare
        Device device = new Device();
        device.setName("device name");
        device.setBrand("device brand");
        device.setCreationTime(1710849243328l); // hard coded date - should not be updated 2023-03-19 11:54
        device = this.deviceRepository.save(device);

        DeviceRequest deviceRequest = new DeviceRequest();
        deviceRequest.setBrand("changed brand");

        String jsonRequest = this.objectMapper.writeValueAsString(deviceRequest);

        // Execute and Assert
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/device/"+device.getId())
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        Assert.assertNotNull(json);
        DeviceResponse deviceResponse = this.objectMapper.readValue(json, DeviceResponse.class);
        Assert.assertEquals(device.getId(), deviceResponse.getId());
        Assert.assertEquals(device.getCreationTime(), deviceResponse.getCreationTime()); // not changed
        Assert.assertEquals(device.getName(), deviceResponse.getName()); // Not changed
        Assert.assertEquals(deviceRequest.getBrand(), deviceResponse.getBrand());
    }


    @Test
    public void deleteDevice_success() throws Exception {
        // Prepare
        Device device = new Device();
        device.setName("device name");
        device.setBrand("device brand");
        device.setCreationTime(1710849243328l); // hard coded date - should not be updated 2023-03-19 11:54
        device = this.deviceRepository.save(device);

        // Execute and Assert
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/device/"+device.getId()))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
                .andReturn();
    }

    @Test
    public void deleteDevice_notExists_success() throws Exception {
        // Execute and Assert
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/device/"+99))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
                .andReturn();
    }


}
