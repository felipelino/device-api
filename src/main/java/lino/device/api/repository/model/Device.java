package lino.device.api.repository.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "DEVICE")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "BRAND")
    private String brand;

    @Column(name = "CREATION_TIME")
    private Long creationTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device device)) return false;
        return Objects.equals(id, device.id) && Objects.equals(name, device.name) && Objects.equals(brand, device.brand) && Objects.equals(creationTime, device.creationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, brand, creationTime);
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", creationTime=" + creationTime +
                '}';
    }
}
