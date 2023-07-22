package ro.msg.learning.shop.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.domain.StockId;
import ro.msg.learning.shop.dto.StockDto;
import ro.msg.learning.shop.dto.StockIDsDto;
import ro.msg.learning.shop.mapper.StockMapper;
import ro.msg.learning.shop.service.StockService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public ResponseEntity<StockDto> createStock(@RequestBody @NonNull StockIDsDto stockIDsDto) {
        try {
            Stock stock = stockService.createStock(stockIDsDto);
            return new ResponseEntity<>(stockMapper.toDto(stock), HttpStatus.CREATED);

        } catch (EntityNotFoundException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
            return ResponseEntity.notFound().build();
        }


    }

    @GetMapping()
    public ResponseEntity<List<StockDto>> getAllStocks() {
        List<StockDto> stockList = stockService.getAllStockCategories();
        return new ResponseEntity<>(stockList, HttpStatus.OK);
    }


    @DeleteMapping("/{productId}/{locationId}")
    public ResponseEntity<String> deleteStock(@PathVariable UUID productId, @PathVariable UUID locationId) {

        try {
            stockService.deleteById(StockId.builder().location(locationId).product(productId).build());
            return ResponseEntity.ok(STOCK_CATEGORY_WITH_ID + productId + " " + locationId + HAS_BEEN_DELETED);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping("/{productId}/{locationId}")
    public ResponseEntity<StockDto> getStockById(@PathVariable UUID productId, @PathVariable UUID locationId) {

        Optional<Stock> stock = stockService.findById(StockId.builder().location(locationId).product(productId).build());
        return stock.map(value -> ResponseEntity.ok(stockMapper.toDto(value))).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/{productId}/{locationId}")
    public ResponseEntity<StockDto> updateStock(
            @PathVariable UUID productId, @PathVariable UUID locationId,
            @RequestBody StockIDsDto updatedStock
    ) {

        try {

            Stock stock = stockService.updateStock(productId, locationId, updatedStock.getQuantity());
            return ResponseEntity.ok(stockMapper.toDto(stock));

        }  catch (EntityNotFoundException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
            return ResponseEntity.notFound().build();
        }

    }
}
