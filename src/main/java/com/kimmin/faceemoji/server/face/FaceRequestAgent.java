package com.kimmin.faceemoji.server.face;

import com.kimmin.faceemoji.server.constant.AccountInfo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * Created by kimmin on 7/19/16.
 */


public class FaceRequestAgent {

    private static final String faceRESTBaseUrl = "https://api.projectoxford.ai/face/v1.0/detect?";
    private static final String faceRESTAttr = "returnFaceId=true&returnFaceLandmarks=true";
    private static final String faceRESTAddr = faceRESTBaseUrl + faceRESTAttr;



    private static final HttpClient client = HttpClientBuilder.create().build();

    public static HttpResponse requestFaceAPI(String imageURL){
        HttpPost post = new HttpPost(faceRESTAddr);
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



}
