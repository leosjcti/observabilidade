package com.leonardo.address.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonardo.address.repository.AddressEntity;
import com.leonardo.address.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class KafkaCustomerConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaCustomerConsumer.class);
    private final AddressRepository addressRepository;

    public KafkaCustomerConsumer(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @KafkaListener(topics = "topic.customer.updated", groupId = "addresses")
    public void listenGroupAddresses(String message) throws JsonProcessingException {
        log.info("m=listenGroupAddresses, step=init, message={}", message);
        CustomerDto customer = new ObjectMapper().readValue(message, CustomerDto.class);
        if (customer.getAddresses() == null || customer.getAddresses().isEmpty()) {
            log.warn("m=listenGroupAddresses, step=not_contains_address");
            return;
        }
        List<AddressEntity> address = customer.getAddresses().stream().peek(it -> it.setCustomerId(customer.getId())).collect(Collectors.toList());
        addressRepository.saveAll(address);
        log.info("m=listenGroupAddresses, step=finished");
    }
}