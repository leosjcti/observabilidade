package com.leonardo.customer.service;

import com.leonardo.customer.httpclient.AddressHttpClient;
import com.leonardo.customer.infra.CustomerUpdateKafkaProducer;
import com.leonardo.customer.repository.AddressEntity;
import com.leonardo.customer.repository.CustomerEntity;
import com.leonardo.customer.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;
    private final AddressHttpClient addressHttpClient; // NOVO
    private final CustomerUpdateKafkaProducer producer;

    public CustomerService(CustomerRepository customerRepository, AddressHttpClient addressHttpClient, CustomerUpdateKafkaProducer producer) {
        this.customerRepository = customerRepository;
        this.addressHttpClient = addressHttpClient; // NOVO
        this.producer = producer;//KAFKA
    }

    public CustomerEntity getById(Long id) {
        log.info("method=getById, id={}", id);
        CustomerEntity customerEntity = customerRepository.findById(id).get();

        // NOVO
        List<AddressEntity> addresses = addressHttpClient.getAddressesForCustomer(customerEntity.getId());
        log.info("method=getById, id={}, addresses={}", id, addresses);
        customerEntity.setAddresses(addresses);
        return customerEntity;
    }

    public CustomerEntity create(CustomerEntity customerEntity) {
        log.info("method=create, customerEntity={}", customerEntity);
        CustomerEntity saved = customerRepository.save(customerEntity);
        producer.send(saved);
        return saved;
    }
}
