package com.kimmin.faceemoji.server.dao.impl;

import com.kimmin.faceemoji.server.dao.UserDAO;
import com.kimmin.faceemoji.server.entity.User;
import org.springframework.stereotype.Repository;

/**
 * Created by kimmin on 8/23/16.
 */

@Repository
public class UserDAOImpl extends GeneralDAOImpl<User> implements UserDAO {

    public UserDAOImpl(){
        super(User.class);
    }




}
