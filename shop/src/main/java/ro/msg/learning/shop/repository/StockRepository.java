package ro.msg.learning.shop.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.domain.Product;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.domain.StockId;
import ro.msg.learning.shop.dto.ProductIdAndQuantity;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Repository
public interface StockRepository extends JpaRepository<Stock, StockId>, JpaSpecificationExecutor<Stock> {
    default List<UUID> findLocationIdsByProductIdsAndStock(List<ProductIdAndQuantity> productIdAndQuantityList, List<Product> products) {
        return this.findAll((Specification<Stock>) (root, query, criteriaBuilder) -> {

            Predicate[] predicates = new Predicate[productIdAndQuantityList.size()];

            for (int i = 0; i < productIdAndQuantityList.size(); i++) {
                predicates[i] = criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("product"), products.get(i)),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("quantity"), productIdAndQuantityList.get(i).getQuantity())
                );
            }

            return criteriaBuilder.or(predicates);
        }).stream().map(stock -> stock.getLocation().getId()).collect(Collectors.toList());
    }

    @Query("    SELECT location" +
            "    FROM Stock" +
            "    WHERE product IN :products" +
            "    GROUP BY location" +
            "    HAVING COUNT(DISTINCT product) =:productsCount")
    List<Location> findLocationIdsByProducts(@Param("products") List<Product> products, @Param("productsCount") Integer productsCount);

}