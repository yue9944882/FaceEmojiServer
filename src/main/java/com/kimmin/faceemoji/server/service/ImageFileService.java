package com.kimmin.faceemoji.server.service;

import com.kimmin.faceemoji.server.constant.AccountInfo;
import com.kimmin.faceemoji.server.emotion.EmotionRequestAgent;
import com.kimmin.faceemoji.server.emotion.EmotionResponseParser;
import com.kimmin.faceemoji.server.emotion.EmotionResult;
import com.kimmin.faceemoji.server.face.FaceRequestAgent;
import com.kimmin.faceemoji.server.face.FaceResponseParser;
import com.kimmin.faceemoji.server.face.FaceResult;
import com.kimmin.faceemoji.server.storage.StorageManager;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.http.HttpResponse;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by kimmin on 7/19/16.
 */


@Service
public class ImageFileService {

    public String uploadImage(String username, InputStream inputStream, long length, String givenUuid){
        String id = StorageManager.getInstance().saveImageToCloud(username, inputStream, length, givenUuid);
        return id;
    }

    public void transformImage(String username, InputStream inputStream, OutputStream outputStream, long length, String givenUuid){
        /** Save the image to Azure Storage **/
        String storageUri = AccountInfo.msAzureBlobEndpoint
                + "/" + username + "/" + givenUuid;
        /** Begin Transforming Emojis **/
        try{
            String id = StorageManager.getInstance().saveImageToCloud(username, inputStream, length, givenUuid);
            inputStream.reset();
            BufferedImage image = ImageIO.read(inputStream);
            int height = image.getHeight();
            int width = image.getWidth();
            HttpResponse resp = EmotionRequestAgent.requestEmotionAPI(storageUri);
            long httplength = resp.getEntity().getContentLength();
            byte[] bytes = new byte[(int) httplength];
            resp.getEntity().getContent().read(bytes);
            String content = new String(bytes);
            HttpResponse fresp = FaceRequestAgent.requestFaceAPI(storageUri);
            byte[] fbytes = new byte[(int) fresp.getEntity().getContentLength()];
            fresp.getEntity().getContent().read(fbytes);
            String fcontent = new String(fbytes);
            List<EmotionResult> results = EmotionResponseParser.parseEmotionResult(content);
            List<FaceResult> fresults = FaceResponseParser.parseFaceResult(fcontent);
            /** Read uploaded binary image **/
            inputStream.reset();
            BufferedImage targetImage = ImageIO.read(inputStream);
            URL urlSVG = null;

            for(int m = 0; m < results.size(); m++){
                EmotionResult result = results.get(m);
                FaceResult fresult = fresults.get(m);
                String emotion = null;
                double score = Double.MIN_VALUE;
                for(Field field : result.getClass().getFields()){
                    if(!(field.getName().equals("width") || field.getName().equals("height")
                            || field.getName().equals("left") || field.getName().equals("top"))){
                        double _score = field.getDouble(result);
                        if(score < _score){
                            score = _score;
                            emotion = field.getName();
                        }
                    }
                }
                Resource resource = null;
                if(emotion.equals("happiness")){
                    resource = new ClassPathResource("emoji/Happiness"
                            + new Double(Math.floor(result.happiness * 10 - 1)).intValue() + ".svg");
                }else{
                    resource = new ClassPathResource("emoji/" + emotion + ".svg");
                }
                /** Transform SVG to PNG **/
                PNGTranscoder pngTranscoder = new PNGTranscoder();
                FileInputStream fileInputStream = new FileInputStream(resource.getFile());
                StringBuffer sb = new StringBuffer();
                String line = "";
                BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
                while((line = br.readLine()) != null){
                    sb.append(line);
                }
                String faceXML = sb.toString().substring(1);
                Document document = DocumentHelper.parseText(faceXML);
                Element root = document.getRootElement();
                if(fresult.gender.equals("female")){
                    Resource knotresource = new ClassPathResource("emoji/bowknot.xml");
                    String knotLine = "";
                    BufferedReader knotbr = new BufferedReader(new InputStreamReader(
                            new FileInputStream(knotresource.getFile())
                    ));
                    StringBuffer knotsb = new StringBuffer();
                    while((knotLine = knotbr.readLine()) != null){
                        knotsb.append(knotLine);
                    }
                    Document knotDoc = DocumentHelper.parseText(knotsb.toString().substring(1));
                    Iterator<Element> iter = knotDoc.getRootElement().elementIterator();
                    while(iter.hasNext()){
                        Element element = iter.next();
                        element.detach();
                        root.add(element);
                    }
                }
                if(fresult.glasses.equals("ReadingGlasses")){
                    Resource readingGlassesResource = new ClassPathResource("emoji/readingGlasses.xml");
                    String glassLine = "";
                    BufferedReader glassbr = new BufferedReader(new InputStreamReader(
                            new FileInputStream((readingGlassesResource.getFile()))
                    ));
                    StringBuffer glasssb = new StringBuffer();
                    while((glassLine = glassbr.readLine()) != null){
                        glasssb.append(glassLine);
                    }
                    Document glassDoc = DocumentHelper.parseText(glasssb.toString().substring(1));
                    Iterator<Element> iter = glassDoc.getRootElement().elementIterator();
                    while(iter.hasNext()){
                        Element element = iter.next();
                        element.detach();
                        root.add(element);
                    }
                }
                if(fresult.glasses.equals("SwimmingGoggles")){
                    Resource readingGlassesResource = new ClassPathResource("emoji/swimmingGoggles.xml");
                    String glassLine = "";
                    BufferedReader glassbr = new BufferedReader(new InputStreamReader(
                            new FileInputStream((readingGlassesResource.getFile()))
                    ));
                    StringBuffer glasssb = new StringBuffer();
                    while((glassLine = glassbr.readLine()) != null){
                        glasssb.append(glassLine);
                    }
                    Document glassDoc = DocumentHelper.parseText(glasssb.toString().substring(1));
                    Iterator<Element> iter = glassDoc.getRootElement().elementIterator();
                    while(iter.hasNext()){
                        Element element = iter.next();
                        element.detach();
                        root.add(element);
                    }
                }
                int age = new Double(fresult.age).intValue();
                String red = Integer.toHexString(255 - age);
                if(red.length() == 1) red = "0" + red;
                String green = Integer.toHexString(255 - age);
                if(green.length() == 1) green = "0" + green;
                String blue = Integer.toHexString(240 - 3 * age >= 0 ? 240 - 3 * age : 0);
                if(blue.length() == 1) blue = "0" + blue;
                document.getRootElement().element("ellipse").attribute("fill").setValue("#" + red + green + blue);
                String newContent = document.asXML().toString();
                String uuid = givenUuid == null ? UUID.randomUUID().toString() : givenUuid;
                File emojiFile = new File(uuid + ".png");
                ByteArrayInputStream bais = new ByteArrayInputStream(newContent.getBytes());
                FileOutputStream fileOutputStream = new FileOutputStream(emojiFile);
                TranscoderInput input = new TranscoderInput(bais);
                TranscoderOutput output = new TranscoderOutput(fileOutputStream);
                pngTranscoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, new Integer(result.width).floatValue());
                pngTranscoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, new Integer(result.height).floatValue());
                pngTranscoder.transcode(input, output);
                /** Overwrite PNG image via offset **/
                BufferedImage emojiImage = ImageIO.read(emojiFile);
                int targetWidth = emojiImage.getWidth();
                int targetHeight = emojiImage.getHeight();
                int[] emojiArray = emojiImage.getRGB(0, 0, targetWidth, targetHeight, null, 0, targetWidth);
                for(int i=0; i<targetHeight; i++){
                    for(int j=0; j<targetWidth; j++){
                        int alpha = emojiArray[i * targetWidth + j] ^ 0x000000ff;
                        if(alpha <0){
                            /** Why < 0? I dont know either... **/
                            targetImage.setRGB((result.left + j), result.top + i, emojiArray[i * targetWidth + j]);
                        }
                    }
                }
                emojiFile.delete();
            }
            ImageIO.write(targetImage, "png", outputStream);
            outputStream.flush();
            inputStream.close();
            outputStream.close();
        }catch (Throwable e){
            e.printStackTrace();
        }
    }


}

