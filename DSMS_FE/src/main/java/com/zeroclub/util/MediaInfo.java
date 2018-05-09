package com.zeroclub.util;

import com.zeroclub.entity.Media;
import com.zeroclub.entity.User;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.VideoAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;

public class MediaInfo {
    public static Media getInfo(MultipartFile tempFile, String type, User user, String path)throws IOException{
        String fileName = UUID.randomUUID().toString();

        String fullName = tempFile.getOriginalFilename();
        int dit = fullName.lastIndexOf(".");

        String name = fullName.substring(0, dit);
        String _type = fullName.substring(dit+1, fullName.length());



        String url = "/resources/media/"+user.getId()+"/"+type+"/"+fileName+"."+_type;
        String imageUrl = "/resources/media/"+user.getId()+"/image/"+fileName+".";

        File file = new File(path+url);
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        tempFile.transferTo(file);
        if (!file.exists())return null;

        File imageFile = file;
        if (type.equals("video")){
            imageUrl = imageUrl + "png";
            imageFile = new File(path+imageUrl);
            VideoAttributes va = new VideoAttributes();
            va.setCodec("png");
            EncodingAttributes ea = new EncodingAttributes();
            ea.setDuration(0.01f);
            ea.setOffset(3f);
            ea.setFormat("image2");
            ea.setVideoAttributes(va);
            Encoder encoder = new Encoder();
            try{
                encoder.encode(file, imageFile, ea);
            }catch (EncoderException e){ }
        }
        else imageUrl = imageUrl + _type;
        if (!imageFile.exists())return null;

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
}