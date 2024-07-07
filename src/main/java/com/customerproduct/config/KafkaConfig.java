package com.customerproduct.config;

import com.customerproduct.constants.AppConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;

import java.util.List;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic topic() {
        return new NewTopic(AppConstants.CUSTOMER_ADD_UPDATE_TOPIC_NAME,1,(short)3);
    }

//    @KafkaListener(topics = AppConstants.CUSTOMER_ADD_UPDATE_TOPIC_NAME,groupId =AppConstants.GROUP_ID)
//    public void BulkUpdateConsumer (String customer) {
//        System.out.println(customer);
//    }
}
