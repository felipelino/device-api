package lino.device.api.repository;

import lino.device.api.repository.model.Device;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceRepositoryTest {

    @Autowired
    private DeviceRepository deviceRepository;

    @Test
    public void findByBrand_whenExists() throws Exception {

        // Prepare
        Device device = new Device();
        device.setName("CustomName");
        device.setBrand("CustomBrand");
        device.setCreationTime(new Date().getTime());
        deviceRepository.save(device);

        // Execute
        Optional<Device> optional = deviceRepository.findByBrand(device.getBrand());

        // Assert
        Assert.assertNotNull(optional);
        Assert.assertTrue(optional.isPresent());
        Assert.assertEquals(device , optional.get());
    }

    @Test
    public void findByBrand_whenNotExists() throws Exception {

        // Prepare
        deviceRepository.deleteAll();

        // Execute
        Optional<Device> optional = deviceRepository.findByBrand("notExistBrand");

        // Assert
        Assert.assertNotNull(optional);
        Assert.assertTrue(optional.isEmpty());
    }
}
