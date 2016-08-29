package com.kimmin.faceemoji.server.face;

import com.kimmin.faceemoji.server.constant.AccountInfo;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by kimmin on 7/19/16.
 */




    /**
     * Created by kimmin on 8/25/16.
     */
public class FaceRequestAgent {


    private static final String emotionRESTBaseUrl = "https://api.projectoxford.ai/face/v1.0/detect";
    private static final String emotionRESTAttr = "?returnFaceLandmarks=true&returnFaceAttributes=age,gender,glasses";
    private static final String emotionRESTAddr = emotionRESTBaseUrl + emotionRESTAttr;


    public static HttpResponse requestFaceAPI(String imageURL){
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(emotionRESTAddr);
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Ocp-Apim-Subscription-Key", AccountInfo.msFaceSubscriptionKey);
        post.setEntity(new StringEntity(
                "{" + "\"url\"" + ":\"" + imageURL + "\"}",
                "UTF-8"
        ));
        try{
            return client.execute(post);
        }catch (IOException ioe){
            ioe.printStackTrace();
            return null;
        }
    }

    public static HttpResponse requestFaceAPI(File pngFile){
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(emotionRESTAddr);
            post.addHeader("Content-Type", "application/octet-stream");
            post.addHeader("Ocp-Apim-Subscription-Key", AccountInfo.msFaceSubscriptionKey);
            FileInputStream fis = new FileInputStream(pngFile);
            int length = fis.available();
            byte[] bytes = new byte[length];
            fis.read(bytes);
            post.setEntity(new ByteArrayEntity(bytes));
            return client.execute(post);
        }catch (Throwable e){
            e.printStackTrace();
            return null;
        }
    }


}
