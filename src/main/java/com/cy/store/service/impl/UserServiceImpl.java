package com.cy.store.service.impl;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.xml.crypto.dsig.DigestMethod;
import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void reg(User user) {
        User result = userMapper.getUserByUsername(user.getUsername());
        if(result != null){
            throw new UsernameDuplicatedException("用户已使用");
        }

        String password = user.getPassword();
        String salt = UUID.randomUUID().toString().toUpperCase();
        String md5Password = getMD5Password(password, salt);
        user.setSalt(salt);
        user.setPassword(md5Password);

        user.setIsDelete(0);
        user.setCreatedUser(user.getUsername());
        user.setModifiedUser(user.getUsername());
        Date date = new Date();
        user.setCreatedTime(date);
        user.setModifiedTime(date);

        final Integer rows = userMapper.insert(user);
        if(rows!=1){
            throw new InsertException("注册异常");
        }

    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.getUserByUsername(username);
        if(user==null || user.getIsDelete() == 1){
            throw  new UserNotFoundException("用户不存在");
        }

        String salt = user.getSalt();
        String md5Password = getMD5Password(password, salt);
        String oldPassword = user.getPassword();
        if(!md5Password.equals(oldPassword)){
            throw new PasswordNotMatchException("密码不匹配");
        }

        User result = new User();
        result.setUid(user.getUid());
        result.setUsername(user.getUsername());
        result.setAvatar(user.getAvatar());


        return result;

    }

    @Override
    public void changePassword(Integer uid, String username, String oldPassword, String newPassword) {
        User user = userMapper.findByUid(uid);
        if(user == null || user.getIsDelete() == 1){
            throw new UserNotFoundException("用户不存在");
        }

        String oldMd5Password = getMD5Password(oldPassword, user.getSalt());
        if(!user.getPassword().equals(oldMd5Password)){
            throw new PasswordNotMatchException("原密码不匹配");
        }

        String newMd5Password = getMD5Password(newPassword, user.getSalt());
        Integer rows = userMapper.updatePasswordByUid(uid, newMd5Password, username, new Date());
        if (rows != 1){
            throw new UpdateException("未知修改密码异常");
        }

    }

    private String getMD5Password(String password, String salt){
        for (int i = 0; i < 3; i++) {
            password = DigestUtils.md5DigestAsHex((salt+password+salt).getBytes()).toUpperCase();
        }

        return password;
    }
}
