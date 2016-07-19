package com.kimmin.faceemoji.server.controller;

import com.kimmin.faceemoji.server.service.ImageFileService;
import com.kimmin.faceemoji.server.storage.StorageManager;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kimmin on 7/19/16.
 */

@Controller
@MultipartConfig(maxFileSize = 4 * 1024 * 1024)
public class ImageController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    ImageFileService imageFileService;


    @RequestMapping(value="/upload_image", method= RequestMethod.POST)
    @ResponseBody
    public String handleImageUpload(@RequestParam("username") String username,
                                    @RequestParam("file") MultipartFile file){
        try{
            byte[] bytes = file.getBytes();
            long length = bytes.length;
            String id = imageFileService.uploadImage(username, new ByteArrayInputStream(bytes), length);
            Map map = new HashMap();
            map.put("status", "success");
            map.put("id", id);
            return objectMapper.writeValueAsString(map);
        }catch (Throwable e){
            e.printStackTrace();
            return null;
        }

    }






}
