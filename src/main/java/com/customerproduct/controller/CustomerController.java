package com.customerproduct.controller;

import com.customerproduct.dto.SearchDto;
import com.customerproduct.model.Customer;
import com.customerproduct.service.CustomerService;
import com.customerproduct.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    KafkaService kafkaService;

    @PostMapping(value = "add-customer")
    public ResponseEntity<?> addOrUpdateCustomer(@RequestBody Customer customer) {
        return customerService.addOrUpdateCustomer(customer);
    }

    @GetMapping(value = "/get-customers")
    public ResponseEntity<?> getAllCustomers(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "5") int limit, @RequestParam Integer client) {
        return customerService.getAllCustomers(offset, limit, client);
    }

    @GetMapping(value = "/get-customer")
    public ResponseEntity<?> getCustomer(@RequestBody SearchDto searchDto) {
        return customerService.getCustomer(searchDto);
    }

    @PostMapping(value = "/add-customer-bulk")
    public ResponseEntity<?> bulkAddOrUpdateCustomer(@RequestBody List<Customer> customers) {
        System.out.println(customers.toString());
        boolean customerAddedUpdatedSuccess = kafkaService.bulkAddOrUpdate(customers);
        System.out.println(customerAddedUpdatedSuccess);
        if (customerAddedUpdatedSuccess) return new ResponseEntity<>("Bulk Add or Update in Initiated", HttpStatus.OK);
        else
            return new ResponseEntity<>("Something went wrong while Bulk Add or Update ", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
