package lino.device.api.dto;

import java.util.Objects;

public class DeviceResponse extends DeviceRequest {

    private Long id;

    private Long creationTime;
    public DeviceResponse(){

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
        if (!(o instanceof DeviceResponse that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(id, that.id) && Objects.equals(creationTime, that.creationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, creationTime);
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("id", id)
                .append("creationTime", creationTime)
                .toString();
    }
}
