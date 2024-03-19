package lino.device.api.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

public class DeviceRequest {

    private String name;
    private String brand;

    public DeviceRequest() {

    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceRequest that)) return false;
        return Objects.equals(name, that.name) && Objects.equals(brand, that.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, brand);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("brand", brand)
                .toString();
    }
}
