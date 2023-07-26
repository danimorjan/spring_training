package ro.msg.learning.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.domain.Product;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.dto.OrderDto;
import ro.msg.learning.shop.dto.ProductIdAndQuantity;
import ro.msg.learning.shop.service.ProductService;
import ro.msg.learning.shop.service.StockService;
import ro.msg.learning.shop.strategies.MostAbundantStrategy;
import ro.msg.learning.shop.strategies.SingleLocationStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class LocationStrategyTests {

    @Mock
    private StockService stockService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private MostAbundantStrategy mostAbundantStrategy;

    @InjectMocks
    private SingleLocationStrategy singleLocationStrategy;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSingleLocationStrategy_ValidOrder() {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);

        UUID secondProductId = UUID.randomUUID();
        Product secondProduct = new Product();
        secondProduct.setId(secondProductId);

        UUID locationId = UUID.randomUUID();
        Location location = new Location();
        location.setId(locationId);

        UUID secondLocationID = UUID.randomUUID();
        Location secondLocation = new Location();
        secondLocation.setId(secondLocationID);

        Stock secondLocationStockFirstProduct = new Stock(product,secondLocation,100);

        Stock secondLocationStockSecondProduct = new Stock(secondProduct, secondLocation, 10);

        List<ProductIdAndQuantity> productIdAndQuantityList = new ArrayList<>();
        productIdAndQuantityList.add(ProductIdAndQuantity.builder().productId(productId).quantity(5).build());
        productIdAndQuantityList.add(ProductIdAndQuantity.builder().productId(secondProductId).quantity(5).build());

        OrderDto orderDto = new OrderDto();
        orderDto.setProductIdAndQuantityList(productIdAndQuantityList);


        when(productService.findById(productId)).thenReturn(Optional.of(product));
        when(productService.findById(secondProductId)).thenReturn(Optional.of(secondProduct));

        when(stockService.findLocationIdsByProductIdsAndStock(eq(productIdAndQuantityList), ArgumentMatchers.anyList()))
                .thenReturn(List.of(secondLocationID, secondLocationID));

        when(stockService.findById(secondLocationID, productId)).thenReturn(Optional.of(secondLocationStockFirstProduct));

        when(stockService.findById(secondLocationID, secondProductId)).thenReturn(Optional.of(secondLocationStockSecondProduct));


        List<Stock> takenStocks = singleLocationStrategy.processOrder(orderDto);

        assertEquals(2, takenStocks.size());
        assertEquals(productId, takenStocks.get(0).getProduct().getId());
        assertEquals(secondLocationID, takenStocks.get(0).getLocation().getId());
        assertEquals(5, takenStocks.get(0).getQuantity());

        assertEquals(secondProductId, takenStocks.get(1).getProduct().getId());
        assertEquals(secondLocationID, takenStocks.get(1).getLocation().getId());
        assertEquals(5, takenStocks.get(1).getQuantity());
    }

    @Test
    void testMostAbundantStrategy_ValidOrder() {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);

        UUID locationId = UUID.randomUUID();
        Location location = new Location();
        location.setId(locationId);

        UUID secondLocationID = UUID.randomUUID();
        Location secondLocation = new Location();
        secondLocation.setId(secondLocationID);

        Stock secondLocationStock = new Stock(product, secondLocation, 100);


        List<ProductIdAndQuantity> productIdAndQuantityList = new ArrayList<>();
        productIdAndQuantityList.add(ProductIdAndQuantity.builder().productId(productId).quantity(5).build());

        OrderDto orderDto = new OrderDto();
        orderDto.setProductIdAndQuantityList(productIdAndQuantityList);


        when(productService.findById(productId)).thenReturn(Optional.of(product));

        when(stockService.findLocationWithHighestStock(product)).thenReturn(secondLocation);

        when(stockService.findById(secondLocationID, productId)).thenReturn(Optional.of(secondLocationStock));

        List<Stock> takenStocks = mostAbundantStrategy.processOrder(orderDto);

        assertEquals(1, takenStocks.size());
        assertEquals(productId, takenStocks.get(0).getProduct().getId());
        assertEquals(secondLocationID, takenStocks.get(0).getLocation().getId());
        assertEquals(5, takenStocks.get(0).getQuantity());
    }

}


