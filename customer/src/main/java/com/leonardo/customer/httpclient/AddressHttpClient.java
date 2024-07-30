package com.leonardo.customer.httpclient;

import com.leonardo.customer.repository.AddressEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class AddressHttpClient {

    private static final Logger log = LoggerFactory.getLogger(AddressHttpClient.class);

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    public List<AddressEntity> getAddressesByCustomerId(Long customerId) {
        log.info("method=getAddressesByCustomerId, customerId={}", customerId);
        return restTemplateBuilder
                .build()
                .getForEntity("http://localhost:8085/addresses/customers/" + customerId, List.class)
                .getBody();
    }

    //Reativo
    public List<AddressEntity> getAddressesForCustomer(Long customerId) {
        WebClient webClient = webClientBuilder.build();
        return webClient.get()
                .uri("http://localhost:8085/addresses/customers/{customerId}", customerId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<AddressEntity>>() {})
                .block(); // Blocking call, consider using reactive approach instead
    }
}
