package ro.msg.learning.shop;

import jakarta.persistence.EntityNotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ro.msg.learning.shop.domain.Customer;
import ro.msg.learning.shop.domain.Product;
import ro.msg.learning.shop.repository.CustomerRepository;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.StockRepository;
import ro.msg.learning.shop.service.OrderService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ro.msg.learning.shop.TestingDataSource.*;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ShopApplication.class)
@ActiveProfiles("test")
public class OrderIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private OrderService orderService;


    @Before
    public void setUpTest() throws Exception {
        mvc.perform(post("/populate")).andExpect(status().isOk());
    }

    @After
    public void tearDownTest() throws Exception {
        mvc.perform(delete("/clear")).andExpect(status().isOk());
    }

    @Test
    public void testCreateNewOrderSuccessfully() {
        Customer customer = customerRepository.findAll().get(0);
        Product product = productRepository.findAll().get(0);

        try {
            JSONArray array = new JSONArray();
            array.put(new JSONObject().put(PRODUCT_ID, product.getId()).put(QUANTITY, 10));
            String jsonString = new JSONObject()
                    .put(CUSTOMER_ID, customer.getId())
                    .put(ADDRESS_COUNTRY, ROMANIA)
                    .put(CREATE_DATE, T_17_09_42_411)
                    .put(ADDRESS_CITY, BUCHAREST)
                    .put(ADDRESS_COUNTY, BUCHAREST)
                    .put(ADDRESS_STREET_ADDRESS, A)
                    .put(PRODUCT_ID_AND_QUANTITY_LIST, array)
                    .toString();
            mvc.perform(post("/order").content(jsonString)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        } catch (Exception exception) {
            fail();
        }

    }

    @Test
    public void testMissingStock() {
        Customer customer = customerRepository.findAll().get(0);

        Product product = productRepository.findAll().get(0);

        try {
            JSONArray array = new JSONArray();
            array.put(new JSONObject().put(PRODUCT_ID, product.getId()).put(QUANTITY, 200));
            String jsonString = new JSONObject()
                    .put(CUSTOMER_ID, customer.getId())
                    .put(ADDRESS_COUNTRY, ROMANIA)
                    .put(CREATE_DATE, T_17_09_42_411)
                    .put(ADDRESS_CITY, BUCHAREST)
                    .put(ADDRESS_COUNTY, BUCHAREST)
                    .put(ADDRESS_STREET_ADDRESS, A)
                    .put(PRODUCT_ID_AND_QUANTITY_LIST, array)
                    .toString();
            mvc.perform(post("/order").content(jsonString)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (EntityNotFoundException entityNotFoundException) {
            assertFalse(entityNotFoundException.getMessage().isEmpty());
        } catch (Exception exception) {
            fail();
        }
    }

    @Test
    public void testProductNotFound() {
        Customer customer = customerRepository.findAll().get(0);

        try {
            JSONArray array = new JSONArray();
            array.put(new JSONObject().put(PRODUCT_ID, 13123123).put(QUANTITY, 1));
            String jsonString = new JSONObject()
                    .put(CUSTOMER_ID, customer.getId())
                    .put(ADDRESS_COUNTRY, ROMANIA)
                    .put(ADDRESS_CITY, BUCHAREST)
                    .put(ADDRESS_COUNTY, BUCHAREST)
                    .put(ADDRESS_STREET_ADDRESS, A)
                    .put(PRODUCT_ID_AND_QUANTITY_LIST, array)
                    .toString();
            mvc.perform(post("/order").content(jsonString)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError());
        } catch (EntityNotFoundException productException) {
            assertFalse(productException.getMessage().isEmpty());
            assertEquals(NO_SUCH_PRODUCT, productException.getMessage());
        } catch (Exception exception) {
            fail();
        }
    }

}
