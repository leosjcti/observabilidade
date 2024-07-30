package com.leonardo.customer.controller;

import com.leonardo.customer.repository.CustomerEntity;
import com.leonardo.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public CustomerEntity getById(@PathVariable("id") Long id) {
        log.info("method=getById, step=starting, id={}", id);
        var customer = customerService.getById(id);
        log.info("method=getById, step=finished, id={}, customer={}", id, customer);
        return customer;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CustomerEntity customerEntity, UriComponentsBuilder ucb) {
        log.info("method=create, step=starting, customerEntity={}", customerEntity);
        var customer = customerService.create(customerEntity);
        log.info("method=create, step=finished, customerId={}", customer.getId());
        return ResponseEntity
                .created(ucb.path("/customers/{id}").buildAndExpand(customer.getId()).toUri())
                .build();
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<Object> notFoundHandleException(Exception e) {
        var map = new HashMap<>();
        map.put("message", "Customer Not Found");
        log.warn("method=notFoundHandleException, step=not_found, e={}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(map);
    }
}