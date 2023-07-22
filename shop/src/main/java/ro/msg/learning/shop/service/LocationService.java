package ro.msg.learning.shop.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.dto.LocationDto;
import ro.msg.learning.shop.mapper.LocationMapper;
import ro.msg.learning.shop.repository.LocationRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LocationService {

    public static final String THE_VALUE_IS_ALREADY_IN_THE_LIST = "The value is already in the list.";
    public static final String ID_INVALID = "ID invalid.";
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationMapper locationMapper;


    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    public List<LocationDto> getAllLocations() {
        return locationRepository.findAll().stream().map(location -> locationMapper.toDto(location)).toList();
    }

    public Boolean existById(UUID locationId) {
        return locationRepository.existsById(locationId);
    }

    public void deleteById(UUID locationId) {
        if (Boolean.FALSE.equals(locationRepository.existsById(locationId))) {
            throw new EntityNotFoundException(ID_INVALID);
        }
        locationRepository.deleteById(locationId);
    }

    public Optional<Location> findById(UUID categoryID) {
        return locationRepository.findById(categoryID);
    }

    public Location updateLocation(UUID categoryID, Location updatedLocation) {
        Location existingLocation = locationRepository.findById(categoryID).orElse(null);

        if (existingLocation == null) {

            throw new EntityNotFoundException(ID_INVALID);
        }

        existingLocation.setName(updatedLocation.getName());

        existingLocation.setAddressCity(updatedLocation.getAddressCity());

        existingLocation.setAddressCountry(updatedLocation.getAddressCountry());

        existingLocation.setAddressStreetAddress(updatedLocation.getAddressStreetAddress());

        return locationRepository.save(existingLocation);
    }

    public Optional<Location> findByName(String name) {
        return locationRepository.findByName(name);
    }

}
