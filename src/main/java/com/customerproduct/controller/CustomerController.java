package com.customerproduct.controller;

import com.customerproduct.dto.SearchDto;
import com.customerproduct.model.Customer;
import com.customerproduct.service.CustomerService;
import com.customerproduct.service.KafkaService;
import com.customerproduct.utils.CustomerProductUtils;
import com.customerproduct.validator.CustomerValidator;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    KafkaService kafkaService;


    private final Logger log = LoggerFactory.getLogger(CustomerController.class);

    @PostMapping(value = "add-customer")
    public ResponseEntity<?> addOrUpdateCustomer(@RequestBody Customer customer) {
        Map<String, Object> response = new HashMap<>();
        try {
            CustomerValidator.isValidCustomer(customer);
            boolean customerAddedOrUpdated = customerService.addOrUpdateCustomer(customer);
            if (customerAddedOrUpdated) {
                response.put("message", "Customer added/updated successfully");
            } else {
                response.put("message", "Customer added/updated failed");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error occurred at addOrUpdateCustomer() ",e);
            response.put("message", "Internal Server Error " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping(value = "/get-customers")
    public ResponseEntity<?> getAllCustomers(@RequestBody SearchDto searchDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            CustomerValidator.validateGetAllCustomersParameters(searchDto);
            log.info("Search DTO {}",searchDto);
            List<Customer> customers = customerService.getAllCustomers(searchDto);
            if (!customers.isEmpty()) {
                response.put("message","Customer found!!");
                response.put("data",customers);
                return ResponseEntity.ok(response);
            } else {
                response.put("message","Customer Not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("Error occurred at getAllCustomer() Method ",e);
            response.put("message", "Internal Server Error " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping(value = "/get-customer")
    public ResponseEntity<?> getCustomer(@RequestBody SearchDto searchDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            CustomerValidator.validateGetCustomer(searchDto);
            List<Customer> customers = customerService.getCustomer(searchDto);
            if (!customers.isEmpty()) {
                response.put("message", "Customers found");
                response.put("data", customers);
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "No customers found !!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("Error occurred at getCustomer() ",e);
            response.put("message", "Internal Server Error " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping(value = "/add-customer-bulk")
    public ResponseEntity<?> bulkAddOrUpdateCustomer(@RequestBody List<Customer> customers) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean customerAddedUpdatedSuccess = kafkaService.bulkAddOrUpdate(customers);
            log.info("Customer Added or Updated : {}",customerAddedUpdatedSuccess);
            if (customerAddedUpdatedSuccess) {
                response.put("message", "Bulk Add or Update Initiated");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "Something went wrong while Bulk Add or Update ");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("Error occurred at bulkAddOrUpdateCustomer() ",e);
            response.put("message", "Internal Server Error " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
