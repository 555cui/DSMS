package com.zeroclub.util;

import java.io.File;
import java.io.IOException;

public class FFMpegCreater implements Runnable{
    private File source;
    private File target;

    public void setEncode(File source, File target){
        this.source = source;
        this.target = target;
    }

    public void run() {
        Runtime ffmpeg = Runtime.getRuntime();
        String cmd="ffmpeg -i "+source.getPath()+" -an -ss 3 -t 0.001 -f image2 -y "+target.getPath();
        try{
            Process process= ffmpeg.exec(cmd);
            process.waitFor();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (InterruptedException e){
            System.out.print("finish");
        }
        finally {
            System.out.print("close");
        }
    }
}
