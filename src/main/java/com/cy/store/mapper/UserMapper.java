package com.cy.store.mapper;

import com.cy.store.entity.User;

import java.util.Date;

public interface UserMapper {

    Integer insert(User user);

    User getUserByUsername(String username);

    Integer updatePasswordByUid(Integer uid, String password, String modifiedUser, Date modifiedTime);

    User findByUid(Integer uid);
}
