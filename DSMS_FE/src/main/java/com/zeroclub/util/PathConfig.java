package com.zeroclub.util;

public class PathConfig {
    private String winPath;
    private String linuxPath;

    public String getPath(){
        if (System.getProperty("os.name").equals("Linux"))
            return linuxPath;
        else return winPath;
    }

    public String getWinPath() {
        return winPath;
    }

    public void setWinPath(String winPath) {
        this.winPath = winPath;
    }

    public String getLinuxPath() {
        return linuxPath;
    }

    public void setLinuxPath(String linuxPath) {
        this.linuxPath = linuxPath;
    }
}
