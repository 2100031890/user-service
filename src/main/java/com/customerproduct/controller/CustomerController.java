package com.customerproduct.controller;

import com.customerproduct.dto.SearchDto;
import com.customerproduct.model.Customer;
import com.customerproduct.service.CustomerService;
import com.customerproduct.service.KafkaService;
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

    @PostMapping(value = "add-customer")
    public ResponseEntity<?> addOrUpdateCustomer(@RequestBody Customer customer) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean customerAddedOrUpdated=customerService.addOrUpdateCustomer(customer);
            if(customerAddedOrUpdated)
            {
                response.put("message", "Customer added/updated successfully");
            }
            else{
                response.put("message","Customer added/updated failed");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.put("message", "Internal Server Error " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @GetMapping(value = "/get-customers")
    public ResponseEntity<?> getAllCustomers(@RequestBody SearchDto searchDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Customer> customers = customerService.getAllCustomers(searchDto);
            if (!customers.isEmpty()) {
                response.put("message", "Customers found");
                response.put("data", customers);
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "No customers found !!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.put("message", "Internal Server Error " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping(value = "/get-customer")
    public ResponseEntity<?> getCustomer(@RequestBody SearchDto searchDto) {
        Map<String, Object> response = new HashMap<>();
        try {
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
            System.out.println(e.getMessage());
            response.put("message", "Internal Server Error "+e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @PostMapping(value = "/add-customer-bulk")
    public ResponseEntity<?> bulkAddOrUpdateCustomer(@RequestBody List<Customer> customers) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean customerAddedUpdatedSuccess = kafkaService.bulkAddOrUpdate(customers);
            System.out.println(customerAddedUpdatedSuccess);
            if (customerAddedUpdatedSuccess) {
                response.put("message", "Bulk Add or Update Initiated");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "Something went wrong while Bulk Add or Update ");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.put("message", "Internal Server Error "+e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

    }
}
