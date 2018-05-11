package com.zeroclub.util;

import java.io.File;
import java.io.IOException;

public class FFMpegCreater implements Runnable{
    private File source;
    private File target;
    private Thread thread;

    public void setEncode(File source, File target){
        this.source = source;
        this.target = target;
    }

    public void run() {
        Runtime ffmpeg = Runtime.getRuntime();
        String cmd="ffmpeg -i "+source.getPath()+" -an -ss 3 -t 0.001 -f image2 -y "+target.getPath();
        try{
            ffmpeg.exec(cmd);
        }
        catch (IOException e){
            System.out.print(e.toString());
        }
    }

    public void start(){
        this.thread = new Thread(this);
        this.thread.start();
    }
}
