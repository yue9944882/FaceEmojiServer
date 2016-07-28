package com.kimmin.faceemoji.server.emotion;

/**
 * Created by kimmin on 7/20/16.
 */
public class EmotionResultBuilder {

    private EmotionResultBuilder(){
    }

    private EmotionResult result = new EmotionResult();

    public static EmotionResultBuilder emotionBuilder(){
        return new EmotionResultBuilder();
    }

    public EmotionResultBuilder setWidth(int width){
        result.width = width;
        return this;
    }

    public EmotionResultBuilder setHeight(int height){
        result.height = height;
        return this;
    }

    public EmotionResultBuilder setTop(int top){
        result.top = top;
        return this;
    }

    public EmotionResultBuilder setLeft(int left){
        result.left = left;
        return this;
    }

    public EmotionResultBuilder setAnger(double anger){
        result.anger = anger;
        return this;
    }

    public EmotionResultBuilder setContempt(double contempt){
        result.contempt = contempt;
        return this;
    }

    public EmotionResultBuilder setDisgust(double disgust){
        result.disgust = disgust;
        return this;
    }

    public EmotionResultBuilder setFear(double fear){
        result.fear = fear;
        return this;
    }

    public EmotionResultBuilder setHappiness(double happiness){
        result.happiness = happiness;
        return this;
    }

    public EmotionResultBuilder setNeutral(double neutral){
        result.neutral = neutral;
        return this;
    }

    public EmotionResultBuilder setSadness(double sadness){
        result.sadness = sadness;
        return this;
    }

    public EmotionResultBuilder setSurprise(double surprise){
        result.surprise = surprise;
        return this;
    }

    public EmotionResult build(){
        return result;
    }


}


