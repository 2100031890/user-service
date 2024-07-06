package com.customerproduct.service;

import com.customerproduct.dto.SearchDto;
import com.customerproduct.model.Customer;
import com.customerproduct.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public ResponseEntity<?> addOrUpdateCustomer(Customer customer) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            boolean result = customerRepository.addOrUpdateCustomer(customer);
            if (result) return new ResponseEntity<>(customer, httpStatus);
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            System.out.println("Exception occurred at addOrUpdateCustomer() Method " + e);
        }
        return new ResponseEntity<>("Error Occurred while saving customer ", httpStatus);
    }

    public ResponseEntity<?> getAllCustomers(int offset, int limit,Integer client) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            if(client<=0){
                httpStatus=HttpStatus.BAD_REQUEST;
                return new ResponseEntity<>("Client id is required ",httpStatus);
            }
            List<Customer> customers = customerRepository.getAllCustomers(offset, limit,client);
            return new ResponseEntity<>(customers, httpStatus);
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            System.out.println("Exception occurred at getAllCustomers() Method " + e);
        }
        return new ResponseEntity<>("Error Occurred while getAllCustomer() Method ", httpStatus);
    }

    public ResponseEntity<?> getCustomer(SearchDto searchDto) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            if(searchDto.getClient()==0) {
                httpStatus=HttpStatus.BAD_REQUEST;
                return new ResponseEntity<>("Client is required",httpStatus);
            }

            List<Customer> customers = customerRepository.getCustomer(searchDto);
            return new ResponseEntity<>(customers, httpStatus);
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>("Error Occurred while getCustomer() Method ", httpStatus);
        }
    }
}
