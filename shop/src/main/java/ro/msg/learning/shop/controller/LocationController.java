package ro.msg.learning.shop.controller;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.dto.LocationDto;
import ro.msg.learning.shop.mapper.LocationMapper;
import ro.msg.learning.shop.service.LocationService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/location")
@RestController
public class LocationController {

    public static final String PRODUCT_CATEGORY_WITH_ID = "Location with ID ";
    public static final String HAS_BEEN_DELETED = " has been deleted.";
    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationMapper locationMapper;

    @PostMapping()
    public ResponseEntity<LocationDto> createLocation(@RequestBody @NonNull LocationDto locationDto) {

        Location location = locationService.createLocation(locationMapper.toEntity(locationDto));
        return new ResponseEntity<>(locationMapper.toDto(location), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        List<LocationDto> locations = locationService.getAllLocations();
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<String> deleteLocation(@PathVariable UUID locationId) {
        locationService.deleteById(locationId);
        return ResponseEntity.ok(PRODUCT_CATEGORY_WITH_ID + locationId + HAS_BEEN_DELETED);
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> getLocationById(@PathVariable UUID locationId) {
        Optional<Location> location = locationService.findById(locationId);
        return location.map(value -> ResponseEntity.ok(locationMapper.toDto(value))).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateLocation(
            @PathVariable UUID locationId,
            @RequestBody LocationDto updatedLocation
    ) {
        Location location = locationService.updateLocation(locationId, locationMapper.toEntity(updatedLocation));
        return ResponseEntity.ok(locationMapper.toDto(location));
    }
}
