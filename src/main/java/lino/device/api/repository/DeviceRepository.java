package lino.device.api.repository;

import lino.device.api.repository.model.Device;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends CrudRepository<Device, Long> {
    Optional<Device> findByBrand(String brand);
}
