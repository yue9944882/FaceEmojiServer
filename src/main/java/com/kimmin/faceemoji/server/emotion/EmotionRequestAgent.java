package com.kimmin.faceemoji.server.emotion;

import com.kimmin.faceemoji.server.constant.AccountInfo;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

/**
 * Created by kimmin on 7/19/16.
 */
public class EmotionRequestAgent {

    private static final String emotionRESTBaseUrl = "https://api.projectoxford.ai/emotion/v1.0/recognize";
    private static final String emotionRESTAttr = "";
    private static final String emotionRESTAddr = emotionRESTBaseUrl + emotionRESTAttr;

    private static final HttpClient client = HttpClientBuilder.create().build();

    public static HttpResponse requestEmotionAPI(String imageURL){
        HttpPost post = new HttpPost(emotionRESTAddr);
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Ocp-Apim-Subscription-Key", AccountInfo.msEmotionSubscriptionKey);
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
