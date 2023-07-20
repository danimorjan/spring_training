package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.domain.StockId;

public interface StockRepository extends JpaRepository<Stock, StockId> {
}