package com.customerproduct.service;

import com.customerproduct.constants.AppConstants;
import com.customerproduct.dto.SearchDto;
import com.customerproduct.model.Customer;
import com.customerproduct.repository.CustomerRepository;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RedissonClient redissonClient;

    public boolean addOrUpdateCustomer(Customer customer) {
        try {
            String cacheKey= AppConstants.CUSTOMER_CACHE_PREFIX+customer.getClient()+customer.getCustomerCode();
            RBucket<Customer> bucket=redissonClient.getBucket(cacheKey);
            bucket.set(customer, Duration.ofMinutes(10));
            return customerRepository.addOrUpdateCustomer(customer);
        } catch (Exception e) {
            log.error("Exception occurred at addOrUpdateCustomer() Method " ,e);
        }
        return false;
    }

    public List<Customer> getAllCustomers(SearchDto searchDto) {
        try {
            String cacheKey= AppConstants.CUSTOMER_CACHE_PREFIX+searchDto.getClient()+searchDto.getCustomerCode();
            RBucket<Customer> bucket=redissonClient.getBucket(cacheKey);
            Customer foundCustomer=bucket.get();
            log.info("Customers in redis is {}",foundCustomer);
            return customerRepository.getAllCustomers(searchDto);
        } catch (Exception e) {
            log.error("Exception occurred at getAllCustomers() Method " , e);
        }
        return null;
    }

    public List<Customer> getCustomer(SearchDto searchDto) {
        try {
            String cacheKey= AppConstants.CUSTOMER_CACHE_PREFIX+searchDto.getClient()+searchDto.getCustomerCode();
            RBucket<Customer> bucket=redissonClient.getBucket(cacheKey);
            Customer foundCustomer=bucket.get();
            log.info("Customers in redis is {}",foundCustomer);
            return customerRepository.getCustomer(searchDto);
        } catch (Exception e) {
            log.error("Exception occurred at getCustomers() Method ",e);
        }
        return null;
    }
}
