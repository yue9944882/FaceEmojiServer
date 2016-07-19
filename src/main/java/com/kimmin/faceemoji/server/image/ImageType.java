package com.kimmin.faceemoji.server.image;

/**
 * Created by kimmin on 7/19/16.
 */
public enum ImageType {

    PNG(0),
    JPEG(1),
    BMP(2);

    private int typeIdx;

    ImageType(int typeIdx){
        this.typeIdx = typeIdx;
    }

    private int getTypeIdx(){
        return this.typeIdx;
    }

}
