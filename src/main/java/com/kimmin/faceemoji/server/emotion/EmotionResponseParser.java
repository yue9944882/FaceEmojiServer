package com.kimmin.faceemoji.server.emotion;

import org.apache.batik.transcoder.image.ImageTranscoder;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kimmin on 7/20/16.
 */
public class EmotionResponseParser {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static List<EmotionResult> parseEmotionResult(String jsonStr){
        try{
            List<EmotionResult> results = new ArrayList<EmotionResult>();
            List list = objectMapper.readValue(jsonStr.getBytes(), List.class);
            for(Object obj : list){
                Map root = (Map) obj;
                Map rectangle = (Map) root.get("faceRectangle");
                Integer left = (Integer) rectangle.get("left");
                Integer top = (Integer) rectangle.get("top");
                Integer height = (Integer) rectangle.get("height");
                Integer width = (Integer) rectangle.get("width");
                Map scores = (Map) root.get("scores");
                Double anger = (Double) scores.get("anger");
                Double contempt = (Double) scores.get("contempt");
                Double disgust = (Double) scores.get("disgust");
                Double fear = (Double) scores.get("fear");
                Double happiness = (Double) scores.get("happiness");
                Double neutral = (Double) scores.get("neutral");
                Double sadness = (Double) scores.get("sadness");
                Double surprise = (Double) scores.get("surprise");
                EmotionResult result = EmotionResultBuilder.emotionBuilder()
                        .setLeft(left)
                        .setTop(top)
                        .setWidth(width)
                        .setHeight(height)
                        .setAnger(anger)
                        .setContempt(contempt)
                        .setDisgust(disgust)
                        .setFear(fear)
                        .setHappiness(happiness)
                        .setNeutral(neutral)
                        .setSadness(sadness)
                        .setSurprise(surprise)
                        .build();
                results.add(result);
            }
            return results;
        } catch (Throwable e){
            e.printStackTrace();
            return null;
        }
    }

}
