package com.kimmin.faceemoji.server.test.azure.emotion;

import com.kimmin.faceemoji.server.emotion.EmotionRequestAgent;
import com.kimmin.faceemoji.server.face.FaceRequestAgent;
import org.apache.http.HttpResponse;
import org.junit.Test;

/**
 * Created by kimmin on 7/19/16.
 */
public class SimpleEmotionAPITest {

    @Test
    public void testEmotionAPI(){

        HttpResponse resp = EmotionRequestAgent.requestEmotionAPI("http://media.npr.org/assets/img/2014/09/26/istock_000019317556large_sq-b51a29cfc009594c39e76f1641f1c1df4a22fd90-s1400.jpg");
        System.out.println(resp.toString());

    }


}
