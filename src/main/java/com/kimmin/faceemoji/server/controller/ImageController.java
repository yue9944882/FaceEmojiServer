package com.kimmin.faceemoji.server.controller;

import com.kimmin.faceemoji.server.constant.Util;
import com.kimmin.faceemoji.server.service.ImageFileService;
import com.kimmin.faceemoji.server.storage.StorageManager;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by kimmin on 7/19/16.
 */

@Controller
@RequestMapping(value = "/image", method = RequestMethod.POST)
@MultipartConfig(maxFileSize = 4 * 1024 * 1024)
public class ImageController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ImageFileService imageFileService;


    @RequestMapping(value="/upload/{username}", method= RequestMethod.POST)
    @ResponseBody
    public String handleImageUpload(@PathVariable("username") String username,
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    @RequestParam("file") MultipartFile file){
        try{
            byte[] bytes = file.getBytes();
            long length = bytes.length;

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            String uuid = UUID.randomUUID().toString();

            imageFileService.transformImage(username, new ByteArrayInputStream(bytes), os, length, uuid);

            os.flush();

            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

            String uri = imageFileService.uploadImage("p" + username, is, os.size(), uuid);

            os.close();
            is.close();

            return uri;

        }catch (Throwable e){
            e.printStackTrace();
            return Util.RESP_FAILURE;
        }

    }


    @RequestMapping(value = "/echo", method = RequestMethod.POST)
    @ResponseBody
    public String echoHandler(){
        return "echo";
    }


    @RequestMapping(value = "/enum/{username}", method = RequestMethod.POST)
    @ResponseBody
    public String enumAllPhoto(@PathVariable("username") String username){
        List<String> list = StorageManager.getInstance().getPhotoByOwner(username);
        try{
            return objectMapper.writeValueAsString(list);
        }catch (Throwable e){
            e.printStackTrace();
            return Util.RESP_FAILURE;
        }
    }

    @RequestMapping(value = "/delete/{username}", method = RequestMethod.POST)
    @ResponseBody
    public String deletePhoto(@PathVariable("username") String username,
                              @RequestBody Map<String, Object> map){
        String key = (String) map.get("key");
        if(key == null) return Util.RESP_FAILURE;
        StorageManager.getInstance().deletePhoto(username, key);
        return Util.RESP_SUCCESS;
    }




}
