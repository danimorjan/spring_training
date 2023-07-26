package ro.msg.learning.shop.service;

import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.Customer;
import ro.msg.learning.shop.repository.CustomerRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService implements UserDetailsService {

    public static final String USER = "USER";
    public static final String USER1 = "User ";
    public static final String NOT_FOUND = " not found";
    public static final String CUSTOMER_ALREADY_EXISTS = "Customer already exists";
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Customer createCustomer(Customer customer) {
        Optional<Customer> existingCustomer=customerRepository.findByUsername(customer.getUsername());
        if(existingCustomer.isEmpty()){
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            return customerRepository.save(customer);
        }
        else{
            throw new EntityExistsException(CUSTOMER_ALREADY_EXISTS);
        }
    }

    public Optional<Customer> findById(UUID categoryID) {
        return customerRepository.findById(categoryID);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> user = customerRepository.findByUsername(username);
        if (user.isPresent()) {
            return User
                    .withUsername(user.get().getUsername())
                    .authorities(USER)
                    .passwordEncoder(passwordEncoder::encode)
                    .password(user.get().getPassword())
                    .build();
        }
        throw new UsernameNotFoundException(USER1 + username + NOT_FOUND);
    }
}
