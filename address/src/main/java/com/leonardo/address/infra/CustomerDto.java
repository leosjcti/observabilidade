package com.leonardo.address.infra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.leonardo.address.repository.AddressEntity;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDto {
    private Long id;
    private List<AddressEntity> addresses;

    public Long getId() { return id; }
    public List<AddressEntity> getAddresses() {return addresses;}
    public void setId(Long id) { this.id = id; }
    public void setAddresses(List<AddressEntity> addresses) {this.addresses = addresses;}

}