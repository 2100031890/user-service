package com.customerproduct.service;

import com.customerproduct.constants.AppConstants;
import com.customerproduct.model.Customer;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public boolean bulkAddOrUpdate(List<Customer> customers) {
        try {
            kafkaTemplate.send(AppConstants.CUSTOMER_ADD_UPDATE_TOPIC_NAME,customers.toString());
            return true;
        } catch (Exception e) {
            System.out.println("Exception occurred at Kafka Service while adding or updating customers " + e);
        }
        return false;
    }

}
