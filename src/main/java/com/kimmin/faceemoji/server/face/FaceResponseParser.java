package com.kimmin.faceemoji.server.face;

import org.apache.batik.transcoder.image.ImageTranscoder;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kimmin on 7/20/16.
 */
public class FaceResponseParser {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static List<FaceResult> parseFaceResult(String jsonStr){
        try{
            List<FaceResult> results = new ArrayList<FaceResult>();
            List list = objectMapper.readValue(jsonStr.getBytes(), List.class);
            for(Object obj : list){
                Map root = (Map) obj;
                Map rectangle = (Map) root.get("faceRectangle");
                Integer left = (Integer) rectangle.get("left");
                Integer top = (Integer) rectangle.get("top");
                Integer height = (Integer) rectangle.get("height");
                Integer width = (Integer) rectangle.get("width");
                Map scores = (Map) root.get("faceLandmarks");
                Map attrs = (Map) root.get("faceAttributes");
                Double plx = (Double) ((Map) scores.get("pupilLeft")).get("x");
                Double ply = (Double) ((Map) scores.get("pupilLeft")).get("y");
                Double prx = (Double) ((Map) scores.get("pupilRight")).get("x");
                Double pry = (Double) ((Map) scores.get("pupilRight")).get("y");
                Double mlx = (Double) ((Map) scores.get("mouthLeft")).get("x");
                Double mly = (Double) ((Map) scores.get("mouthLeft")).get("y");
                Double mrx = (Double) ((Map) scores.get("mouthRight")).get("x");
                Double mry = (Double) ((Map) scores.get("mouthRight")).get("y");
                FaceResult result = new FaceResult();
                result.left = left;
                result.top = top;
                result.height = height;
                result.width = width;

                result.pupilLeftX = plx;
                result.pupilLeftY = ply;
                result.pupilRightX = prx;
                result.pupilRightY = pry;
                result.mouthLeftX = mlx;
                result.mouthLeftY = mly;
                result.mouthRightX = mrx;
                result.mouthRightY = mry;

                double age = (Double) attrs.get("age");
                String gender = (String) attrs.get("gender");
                String glasses = (String) attrs.get("glasses");

                result.glasses = glasses;
                result.age = age;
                result.gender = gender;

                results.add(result);
            }
            return results;
        } catch (Throwable e){
            e.printStackTrace();
            return null;
        }
    }

}
