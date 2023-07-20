package ro.msg.learning.shop.controller;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.dto.LocationGetDto;
import ro.msg.learning.shop.mapper.LocationMapper;
import ro.msg.learning.shop.service.LocationService;

import java.util.List;
import java.util.UUID;

@RequestMapping("/location")
@RestController
@Validated
public class LocationController {

    public static final String PRODUCT_CATEGORY_WITH_ID = "Location with ID ";
    public static final String HAS_BEEN_DELETED = " has been deleted.";
    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationMapper locationMapper;

    @PostMapping()
    public ResponseEntity<LocationGetDto> createLocation(@RequestBody @NonNull LocationGetDto body) {
        try {

            Location location = locationService.createLocation(locationMapper.toEntity(body));
            return new ResponseEntity<>(locationMapper.toGetDto(location), HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping()
    public ResponseEntity<List<LocationGetDto>> getAllLocations() {
        List<Location> productCategories = locationService.getAllLocations();
        return new ResponseEntity<>(productCategories.stream().map(location -> locationMapper.toGetDto(location)).toList(), HttpStatus.OK);
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<String> deleteLocation(@PathVariable UUID locationId) {
        try {
            locationService.deleteById(locationId);
            return ResponseEntity.ok(PRODUCT_CATEGORY_WITH_ID + locationId + HAS_BEEN_DELETED);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationGetDto> getLocationById(@PathVariable UUID locationId) {
        Location location = locationService.findById(locationId).orElse(null);

        if (location == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(locationMapper.toGetDto(location));
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationGetDto> updateLocation(
            @PathVariable UUID locationId,
            @RequestBody LocationGetDto updatedLocation
    ) {
        try {

            Location location = locationService.updateLocation(locationId, locationMapper.toEntity(updatedLocation));
            return ResponseEntity.ok(locationMapper.toGetDto(location));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

    }
}
