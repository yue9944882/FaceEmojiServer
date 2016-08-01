package com.kimmin.faceemoji.server.service;

import com.kimmin.faceemoji.server.constant.AccountInfo;
import com.kimmin.faceemoji.server.emotion.EmotionRequestAgent;
import com.kimmin.faceemoji.server.emotion.EmotionResponseParser;
import com.kimmin.faceemoji.server.emotion.EmotionResult;
import com.kimmin.faceemoji.server.storage.StorageManager;
import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;
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

    public void transformImage(String username, InputStream inputStream, OutputStream outputStream, long length){
        /** Save the image to Azure Storage **/
        String id = StorageManager.getInstance().saveImageToCloud(username, inputStream, length);
        String storageUri = AccountInfo.msAzureBlobEndpoint
                + "/" + username + "/" + id;
        /** Begin Transforming Emojis **/
        try{
            BufferedImage image = ImageIO.read(inputStream);
            int height = image.getHeight();
            int width = image.getWidth();
            HttpResponse resp = EmotionRequestAgent.requestEmotionAPI(storageUri);
            long httplength = resp.getEntity().getContentLength();
            byte[] bytes = new byte[(int) httplength];
            resp.getEntity().getContent().read(bytes);
            String content = new String(bytes);
            List<EmotionResult> results = EmotionResponseParser.parseEmotionResult(content);

            /** Read uploaded binary image **/
            BufferedImage targetImage = ImageIO.read(inputStream);
            URL urlSVG = null;

            for(EmotionResult result : results){

                String emotion = null;
                double score = Double.MIN_VALUE;
                for(Field field : result.getClass().getFields()){
                    if(field.getName().equals("width") && field.getName().equals("height")
                            && field.getName().equals("left") && field.getName().equals("top")){
                        double _score = field.getDouble(result);
                        if(score < _score){
                            score = _score;
                            emotion = field.getName();
                        }
                    }
                }
                if(emotion.equals("happiness")){
                    urlSVG = Thread.currentThread().getContextClassLoader().getResource("happiness"
                        + new Double(Math.floor(result.happiness * 10 - 1)).intValue() + ".svg");
                }else{
                    urlSVG = Thread.currentThread().getContextClassLoader().getResource(emotion + ".svg");
                }
                /** Transform SVG to PNG **/
                PNGTranscoder pngTranscoder = new PNGTranscoder();
                FileInputStream fileInputStream = new FileInputStream(new File(urlSVG.getPath()));
                UUID uuid = UUID.randomUUID();
                File emojiFile = new File(uuid.toString() + ".png");
                FileOutputStream fileOutputStream = new FileOutputStream(emojiFile);
                TranscoderInput input = new TranscoderInput(fileInputStream);
                TranscoderOutput output = new TranscoderOutput(fileOutputStream);
                pngTranscoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, new Integer(result.width).floatValue());
                pngTranscoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, new Integer(result.height).floatValue());
                pngTranscoder.transcode(input, output);
                /** Overwrite PNG image via offset **/
                BufferedImage emojiImage = ImageIO.read(emojiFile);
                int[] emojiArray = emojiImage.getRGB(0, 0, width, height, null, 0, width);
                for(int i=0; i<height; i++){
                    for(int j=0; j<width; j++){
                        int alpha = emojiArray[i * width + j] ^ 0x000000ff;
                        if(alpha <0){
                            /** Why < 0? I dont know either... **/
                            targetImage.setRGB((result.left + j), result.top + i, emojiArray[i * width + j]);
                        }
                    }
                }
            }
            ImageIO.write(targetImage, "png", outputStream);
            outputStream.flush();
        }catch (Throwable e){
            e.printStackTrace();
        }
    }


}
