package com.customerproduct.repository;

import com.customerproduct.dto.SearchDto;
import com.customerproduct.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CustomerRepository {
    boolean addOrUpdateCustomer(Customer customer);

    List<Customer> getAllCustomers(SearchDto searchDto);

    List<Customer> getCustomer(SearchDto searchDto);

    boolean addOrUpdateCustomerBulk(Customer customer);
}
