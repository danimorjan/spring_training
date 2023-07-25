package ro.msg.learning.shop.controller.test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.service.test.TestService;

@RestController
@Profile("test")
public class TestController {
    @Autowired
    private TestService testService;

    @PostMapping("/populate")
    public ResponseEntity<String> populate() {
        testService.populate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clear() {
        testService.clear();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
