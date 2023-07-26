package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.msg.learning.shop.domain.Location;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {
    Optional<Location> findByName(String name);
}