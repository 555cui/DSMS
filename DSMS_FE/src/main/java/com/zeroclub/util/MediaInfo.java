package com.zeroclub.util;

import com.zeroclub.entity.Media;
import com.zeroclub.entity.User;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.VideoAttributes;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;

public class MediaInfo {
    private static Logger logger = Logger.getLogger(MediaInfo.class);
    public static Media getInfo(MultipartFile tempFile, String type, User user, String path) {
        String fileName = UUID.randomUUID().toString();

        String fullName = tempFile.getOriginalFilename();
        int dit = fullName.lastIndexOf(".");

        String name = fullName.substring(0, dit);
        String _type = fullName.substring(dit+1, fullName.length());



        String url = "/resources/media/"+user.getId()+"/"+type+"/"+fileName+"."+_type;
        String imageUrl = "/resources/media/"+user.getId()+"/image/"+fileName+".";

        File file = new File(path+url);
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        try {
            tempFile.transferTo(file);
        }catch (IOException e){
            logger.error(e, e.getCause());
        }
        if (!file.exists())return null;

        File imageFile = file;
        if (type.equals("video")){
            imageUrl = imageUrl + "png";
            imageFile = new File(path+imageUrl);
            if (!imageFile.getParentFile().exists())imageFile.getParentFile().mkdirs();
            logger.info(imageFile.getPath());
            VideoAttributes va = new VideoAttributes();
            //va.setCodec("png");
            EncodingAttributes ea = new EncodingAttributes();
            ea.setDuration(0.001f);
            ea.setOffset(3f);
            ea.setFormat("image2");

            ea.setVideoAttributes(va);
            Encoder encoder = new Encoder();
            try{
                logger.info("start get image");
                encoder.encode(file, imageFile, ea);
            }catch (EncoderException e){
                logger.error(e, e.getCause());
            }
        }
        else imageUrl = imageUrl + _type;
        logger.info("check file");
        if (!imageFile.exists()){
            file.delete();
            return null;
        }

        try {
            BufferedImage bi = ImageIO.read(imageFile);
            Media media = new Media();
            media.setName(name);
            media.setUser(user);
            media.setUrl(url);
            media.setImageUrl(imageUrl);
            media.setEtcData(0);
            media.setType(type);
            media.set_type(_type);
            media.setWidth(bi.getWidth());
            media.setHeight(bi.getHeight());

            return media;
        }
        catch (IOException e){
            logger.error(e, e.getCause());
        }
        return null;
    }
    public static boolean check(MultipartFile tempFile){
        try {
            File f = new File("/home/dsms/test.mp4");
            tempFile.transferTo(f);
            EncodingAttributes ea = new EncodingAttributes();
            ea.setDuration(0.001f);
            ea.setOffset(3f);
            ea.setFormat("image2");
            ea.setVideoAttributes(new VideoAttributes());
            Encoder en = new Encoder();
            File target = new File("/home/dsms/test.png");
            en.encode(f, target, ea);
            return target.exists();
        }catch (IOException e){
            logger.error(e, e.getCause());
        }catch (EncoderException e){
            logger.error(e, e.getCause());
        }
        return false;
    }
}