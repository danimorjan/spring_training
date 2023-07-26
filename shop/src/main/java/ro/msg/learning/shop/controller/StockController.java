package ro.msg.learning.shop.controller;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.domain.StockId;
import ro.msg.learning.shop.dto.StockIDsDto;
import ro.msg.learning.shop.mapper.StockMapper;
import ro.msg.learning.shop.service.StockService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/stock")
@RestController
public class StockController {

    public static final String STOCK_CATEGORY_WITH_ID = "Stock with ID ";
    public static final String HAS_BEEN_DELETED = " has been deleted.";

    @Autowired
    private StockService stockService;

    @Autowired
    private StockMapper stockMapper;

    @PostMapping()
    public ResponseEntity<StockIDsDto> createStock(@RequestBody @NonNull StockIDsDto stockIDsDto) {
        Stock stock = stockService.createStock(stockIDsDto);
        return new ResponseEntity<>(stockMapper.toDto(stock), HttpStatus.CREATED);

    }

    @GetMapping()
    public ResponseEntity<List<StockIDsDto>> getAllStocks() {
        List<StockIDsDto> stockList = stockService.getAllStockCategories();
        return new ResponseEntity<>(stockList, HttpStatus.OK);
    }


    @DeleteMapping("/{productId}/{locationId}")
    public ResponseEntity<String> deleteStock(@PathVariable UUID productId, @PathVariable UUID locationId) {
        stockService.deleteById(StockId.builder().location(locationId).product(productId).build());
        return ResponseEntity.ok(STOCK_CATEGORY_WITH_ID + productId + " " + locationId + HAS_BEEN_DELETED);
    }

    @GetMapping("/{productId}/{locationId}")
    public ResponseEntity<StockIDsDto> getStockById(@PathVariable UUID productId, @PathVariable UUID locationId) {

        Optional<Stock> stock = stockService.findById(locationId, productId);
        return stock.map(value -> ResponseEntity.ok(stockMapper.toDto(value))).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/{productId}/{locationId}")
    public ResponseEntity<StockIDsDto> updateStock(
            @PathVariable UUID productId, @PathVariable UUID locationId,
            @RequestBody StockIDsDto updatedStock
    ) {
        Stock stock = stockService.updateStock(productId, locationId, updatedStock.getQuantity());
        return ResponseEntity.ok(stockMapper.toDto(stock));

    }
}
