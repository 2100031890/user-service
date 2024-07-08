package com.customerproduct.service;

import com.customerproduct.constants.AppConstants;
import com.customerproduct.model.Customer;
import com.customerproduct.validator.CustomerValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaService {


    @Autowired
    private KafkaTemplate<String, Customer> kafkaTemplate;

    private final Logger log = LoggerFactory.getLogger(KafkaService.class);

    public boolean bulkAddOrUpdate(List<Customer> customers) {
        try {
            for (Customer customer : customers) {
                CustomerValidator.isValidCustomer(customer);
                kafkaTemplate.send(AppConstants.CUSTOMER_ADD_UPDATE_TOPIC_NAME, customer);
            }
            return true;
        } catch (Exception e) {
            log.error("Exception occurred at bulkAddOrUpdate() Method in Kafka service " ,e);
        }
        return false;
    }
}
