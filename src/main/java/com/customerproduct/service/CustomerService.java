package com.customerproduct.service;

import com.customerproduct.dto.SearchDto;
import com.customerproduct.model.Customer;

import java.util.List;

public interface CustomerService {
    public boolean addOrUpdateCustomer(Customer customer);

    public List<Customer> getAllCustomers(SearchDto searchDto);

    public List<Customer> getCustomer(SearchDto searchDto);

}
