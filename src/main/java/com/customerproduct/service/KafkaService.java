package com.customerproduct.service;

import com.customerproduct.constants.AppConstants;
import com.customerproduct.model.Customer;
import com.customerproduct.validator.CustomerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaService {

    @Autowired
    CustomerValidator customerValidator;

    @Autowired
    private KafkaTemplate<String, Customer> kafkaTemplate;

    public boolean bulkAddOrUpdate(List<Customer> customers) {
        try {
            for (Customer customer : customers) {
                customerValidator.isValidCustomer(customer);
                kafkaTemplate.send(AppConstants.CUSTOMER_ADD_UPDATE_TOPIC_NAME, customer);
            }
            return true;
        } catch (Exception e) {
            System.out.println("Exception occurred at Kafka Service while adding or updating customers " + e);
        }
        return false;
    }
}
