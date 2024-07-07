package com.customerproduct.service;

import com.customerproduct.dto.SearchDto;
import com.customerproduct.model.Customer;
import com.customerproduct.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public boolean addOrUpdateCustomer(Customer customer) {
        try {
            return customerRepository.addOrUpdateCustomer(customer);
        } catch (Exception e) {
            System.out.println("Exception occurred at addOrUpdateCustomer() Method " + e);
        }
        return false;
    }

    public List<Customer> getAllCustomers(SearchDto searchDto) {
        try {
            if (searchDto.getClient() <= 0) {
                throw new Exception("Client Id is required");
            }
            return customerRepository.getAllCustomers(searchDto);
        } catch (Exception e) {
            System.out.println("Exception occurred at getAllCustomers() Method " + e);
        }
        return null;
    }

    public List<Customer> getCustomer(SearchDto searchDto) {
        try {
            if (searchDto.getClient() <= 0) {
                throw new Exception("Client id is required");
            }
            return customerRepository.getCustomer(searchDto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
