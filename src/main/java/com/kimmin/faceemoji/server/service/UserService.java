package com.kimmin.faceemoji.server.service;

import com.kimmin.faceemoji.server.constant.Util;
import com.kimmin.faceemoji.server.dao.UserDAO;
import com.kimmin.faceemoji.server.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Created by kimmin on 8/23/16.
 */


@Service
@Transactional
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public boolean registerUser(String username, String password, String email, String first, String last){
        User user = new User();
        user.setFirstname(first);
        user.setLastname(last);
        user.setEmail(email);
        user.setUsername(username);
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] rawPasswd = md.digest();
            user.setPassword(rawPasswd);
            userDAO.insert(user);
            return true;
        }catch (Throwable e){
            e.printStackTrace();
            return false;
        }

    }

    public boolean loginUser(String username, String password){
        User user = userDAO.queryById(username);
        if(user == null) return false;
        byte[] bytePassword = user.getPassword();
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());
            byte[] compPassword = messageDigest.digest();
            return Arrays.equals(compPassword, bytePassword);
        }catch (Throwable e){
            e.printStackTrace();
            return false;
        }
    }


}
