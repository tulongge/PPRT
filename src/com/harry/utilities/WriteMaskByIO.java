package com.harry.utilities;

import com.harry.domain.Mask;
import com.harry.domain.MaskGenerator;
import com.harry.domain.PlaceHolder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WriteMaskByIO {
    private static BufferedWriter bufferedWriter;
    private static String filePath =
            "E:\\OneDrive - The University of Nottingham\\PPM\\hashcat-6.2.5\\hashcat-6.2.5\\maskByJava.hcmask";

//    static{
//        try {
//            bufferedWriter = new BufferedWriter(new FileWriter(filePath));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static String getFilePath() {
        return filePath;
    }

    public static void writeMaskFile(MaskGenerator mg) throws IOException {
        bufferedWriter = new BufferedWriter(new FileWriter(filePath));
        ArrayList<Mask> allMasks = mg.getAllMasks();
        HashMap<String, String> dict = PlaceHolder.getDict();
        try {
            for (Mask mask : allMasks) {
                String[] maskContent = mask.getMaskContent();
                //如果有自定义字符集
                for (int i = 1; i<PlaceHolder.getCustomIndex(); i++){
                    bufferedWriter.write(dict.get(String.valueOf(i))+",");
                }
//                for (String s : maskContent) {
//                    if (s.length() == 2){
//                        if (!s.substring(1).equals("a") && !s.substring(1).equals("l") && !s.substring(1).equals("u")
//                                && !s.substring(1).equals("s") && !s.substring(1).equals("d")){
//                            String customSubChar = PlaceHolder.getDict().get(s.substring(1));
//                            bufferedWriter.write(customSubChar+",");
//                            break;
//                        }
//                    }
//                }
                for (String s : maskContent) {
                    bufferedWriter.write(s);
                }
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void flush(){
        try {
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(){
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
