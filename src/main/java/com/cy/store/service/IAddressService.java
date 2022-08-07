package com.cy.store.service;

import com.cy.store.entity.Address;

public interface IAddressService {
    void addNewAddress(Integer uid, String username, Address address);
}
