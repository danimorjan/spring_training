package ro.msg.learning.shop.mapper;

import org.springframework.stereotype.Component;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.dto.LocationDto;
@Component
public class LocationMapper {
    public Location toEntity(LocationDto locationDto) {
        return Location.builder()
                .name(locationDto.getName())
                .addressCity(locationDto.getAddressCity())
                .addressCountry(locationDto.getAddressCountry())
                .addressStreetAddress(locationDto.getAddressStreetAddress())
                .build();
    }

    public LocationDto toDto(Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .addressCity(location.getAddressCity())
                .addressCountry(location.getAddressCountry())
                .addressStreetAddress(location.getAddressStreetAddress())
                .name(location.getName())
                .build();
    }
}
