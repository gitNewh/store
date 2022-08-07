package com.cy.store.mapper;

import com.cy.store.entity.Address;

public interface AddressMapper {
    Integer insert(Address address);
    Integer countByUid(Integer uid);
}
