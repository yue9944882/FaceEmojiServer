package com.kimmin.faceemoji.server.service;

import com.kimmin.faceemoji.server.storage.StorageManager;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

/**
 * Created by kimmin on 7/19/16.
 */


@Service
public class ImageFileService {

    public String uploadImage(String username, InputStream inputStream, long length){

        String id = StorageManager.getInstance().saveImageToCloud(username, inputStream, length);
        return id;

    }





}
