package ro.msg.learning.shop.controller;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.domain.StockId;
import ro.msg.learning.shop.dto.StockCreateDto;
import ro.msg.learning.shop.dto.StockGetDto;
import ro.msg.learning.shop.mapper.StockMapper;
import ro.msg.learning.shop.service.StockService;

import java.util.List;
import java.util.UUID;

@RequestMapping("/stock")
@RestController
@Validated
public class StockController {

    public static final String STOCK_CATEGORY_WITH_ID = "Stock with ID ";
    public static final String HAS_BEEN_DELETED = " has been deleted.";

    @Autowired
    private StockService stockService;

    @Autowired
    private StockMapper stockMapper;

    @PostMapping()
    public ResponseEntity<StockGetDto> createStock(@RequestBody @NonNull StockCreateDto body) {
        Stock stock = stockService.createStock(body);
        return new ResponseEntity<>(stockMapper.toGetDto(stock), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<StockGetDto>> getAllStocks() {
        List<Stock> stockList = stockService.getAllStockCategories();
        return new ResponseEntity<>(stockList.stream().map(stock -> stockMapper.toGetDto(stock)).toList(), HttpStatus.OK);
    }


    @DeleteMapping("/{productId}/{locationId}")
    public ResponseEntity<String> deleteStock(@PathVariable UUID productId, @PathVariable UUID locationId) {

        try {
            stockService.deleteById(StockId.builder().location(locationId).product(productId).build());
            return ResponseEntity.ok(STOCK_CATEGORY_WITH_ID + productId + " " + locationId + HAS_BEEN_DELETED);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{productId}/{locationId}")
    public ResponseEntity<StockGetDto> getStockById(@PathVariable UUID productId, @PathVariable UUID locationId) {
        Stock stock = stockService.findById(StockId.builder().location(locationId).product(productId).build()).orElse(null);

        if (stock == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(stockMapper.toGetDto(stock));
    }

    @PutMapping("/{productId}/{locationId}")
    public ResponseEntity<StockGetDto> updateStock(
            @PathVariable UUID productId, @PathVariable UUID locationId,
            @RequestBody StockCreateDto updatedStock
    ) {

        try {

            Stock stock = stockService.updateStock(productId, locationId, updatedStock.getQuantity());
            return ResponseEntity.ok(stockMapper.toGetDto(stock));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

    }
}
