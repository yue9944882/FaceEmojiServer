package com.kimmin.faceemoji.server.controller;

import com.kimmin.faceemoji.server.constant.Util;
import com.kimmin.faceemoji.server.dao.UserDAO;
import com.kimmin.faceemoji.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by kimmin on 8/23/16.
 */

@Controller
@RequestMapping(value = "/auth", method = {RequestMethod.POST, RequestMethod.GET})
public class AuthController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String verifyUser(@RequestBody Map<String, Object> content){
        String username = (String) content.get("username");
        String password = (String) content.get("password");
        if(userService.loginUser(username, password)){
            return Util.RESP_SUCCESS;
        }else{
            return Util.RESP_FAILURE;
        }
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public String registerUser(@RequestBody Map<String, Object> content){
        String username = (String) content.get("username");
        String password = (String) content.get("password");
        String firstname = (String) content.get("firstname");
        String lastname = (String) content.get("lastname");
        String email = (String) content.get("email");
        if(userService.registerUser(username, password, email, firstname, lastname)){
            return Util.RESP_SUCCESS;
        }else{
            return Util.RESP_FAILURE;
        }
    }

}
