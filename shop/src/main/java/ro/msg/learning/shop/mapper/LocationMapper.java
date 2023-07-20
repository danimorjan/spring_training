package ro.msg.learning.shop.mapper;

import org.springframework.stereotype.Component;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.dto.LocationGetDto;
@Component
public class LocationMapper {
    public Location toEntity(LocationGetDto locationGetDto) {
        return Location.builder()
                .name(locationGetDto.getName())
                .addressCity(locationGetDto.getAddressCity())
                .addressCountry(locationGetDto.getAddressCountry())
                .addressStreetAddress(locationGetDto.getAddressStreetAddress())
                .build();
    }

    public LocationGetDto toGetDto(Location location) {
        return LocationGetDto.builder()
                .id(location.getId())
                .addressCity(location.getAddressCity())
                .addressCountry(location.getAddressCountry())
                .addressStreetAddress(location.getAddressStreetAddress())
                .name(location.getName())
                .build();
    }
}
