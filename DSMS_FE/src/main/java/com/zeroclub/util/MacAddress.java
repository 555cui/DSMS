package com.zeroclub.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MacAddress {
    public static String getMacAddress(String ip){
        String macAddr = null;
        try {
            Process process = Runtime.getRuntime().exec("nbtstat -a " + ip);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            Pattern pattern = Pattern.compile("([A-F0-9]{2}-){5}[A-F0-9]{2}");
            Matcher matcher;
            for (String strLine = br.readLine(); strLine != null;
                 strLine = br.readLine()) {
                matcher = pattern.matcher(strLine);
                if (matcher.find()) {
                    macAddr = matcher.group();
                    break;
                }
            }
            if (macAddr==null){
                Process process1 = Runtime.getRuntime().exec("ping" + ip+ "-c 2 && arp -a");
                BufferedReader br1 = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));
                Pattern pattern1 = Pattern.compile("([A-F0-9]{2}-){5}[A-F0-9]{2}");
                Matcher matcher1;
                for (String strLine = br1.readLine(); strLine != null;
                     strLine = br1.readLine()) {
                    matcher1 = pattern1.matcher(strLine);
                    if (matcher1.find()) {
                        macAddr = matcher1.group();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return macAddr;
    }
}
