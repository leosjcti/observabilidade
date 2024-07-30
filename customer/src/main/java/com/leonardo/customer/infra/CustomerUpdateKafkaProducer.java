package com.leonardo.customer.infra;

import com.leonardo.customer.repository.CustomerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class CustomerUpdateKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(CustomerUpdateKafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void send(CustomerEntity customerEntity) {
        log.info("method=send, step=starting, customerEntity={}", customerEntity);
        kafkaTemplate.send("topic.customer.updated", customerEntity).isDone();
        log.info("method=send, step=finished, customerEntity={}", customerEntity);
    }
}
