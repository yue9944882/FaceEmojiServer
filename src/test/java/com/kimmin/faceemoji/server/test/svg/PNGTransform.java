package com.kimmin.faceemoji.server.test.svg;

import com.kimmin.faceemoji.server.emotion.EmotionRequestAgent;
import com.kimmin.faceemoji.server.emotion.EmotionResponseParser;
import com.kimmin.faceemoji.server.emotion.EmotionResult;
import com.kimmin.faceemoji.server.face.FaceRequestAgent;
import com.kimmin.faceemoji.server.face.FaceResponseParser;
import com.kimmin.faceemoji.server.face.FaceResult;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.http.HttpResponse;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by kimmin on 7/20/16.
 */
public class PNGTransform {


    @Test
    public void testTransforming(){

        PNGTranscoder pngTranscoder = new PNGTranscoder();

        URL urlImage = Thread.currentThread().getContextClassLoader().getResource("naiveblue.png");
        URL urlSVG = Thread.currentThread().getContextClassLoader().getResource("Anger.svg");

        HttpResponse resp = FaceRequestAgent.requestFaceAPI(new File(urlImage.getPath()));

        long length = resp.getEntity().getContentLength();

        byte[] bytes = new byte[(int) length];

        try {

            resp.getEntity().getContent().read(bytes);
            String content = new String(bytes);
            List<FaceResult> results = FaceResponseParser.parseFaceResult(content);
            for(FaceResult result : results){
                pngTranscoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, new Float(new Integer(result.width).floatValue()));
                pngTranscoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, new Float(new Integer(result.height).floatValue()));
                TranscoderInput input = new TranscoderInput(new FileInputStream(new File(urlSVG.getPath())));
                OutputStream os = new FileOutputStream("test.png");
                TranscoderOutput output = new TranscoderOutput(os);
                pngTranscoder.transcode(input, output);
                os.flush();
                os.close();
                BufferedImage emojiImage = ImageIO.read(new FileInputStream("test.png"));
                float l = 1.414f;
                int height = emojiImage.getHeight();
                int width = emojiImage.getWidth();
                // ARGB Array
                int[] emojiArray = emojiImage.getRGB(0, 0, width, height, null, 0, width);
                BufferedImage targetImage = ImageIO.read(new FileInputStream(new File(urlImage.getPath())));
                int type = emojiImage.getType();
                int fakeleft = new Float(result.left - result.width * 0.414f).intValue();
                int faketop = new Float(result.top - result.height * 0.414f).intValue();
                for(int i=0; i<height; i++){
                    for(int j=0; j<width; j++){
                        int alpha = emojiArray[i * width + j] ^ 0x000000ff;
                        if(alpha <0){
                            /** Why < 0? I dont know either... **/
                            if(fakeleft + j > 0 && faketop + i > 0) {
                                targetImage.setRGB((fakeleft + j), faketop + i, emojiArray[i * width + j]);
                            }
                        }
                    }
                }
                ImageIO.write(targetImage, "png", new File("testOutput1.png"));
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }


}
