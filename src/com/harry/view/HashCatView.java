package com.harry.view;

import com.harry.domain.*;
import com.harry.utilities.ExecuteHashCat;
import com.harry.utilities.Utility;
import com.harry.utilities.WriteMaskByIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class HashCatView {
    private boolean loop = true;
    private String key = "";//接收用户输入
    private Certainty certainty = new Certainty();//certainty内部维护了一个hashmap用来存放密码长度和可能性
    private ArrayList<MaskTemplate> maskTemplates = new ArrayList<>();//每一个长度的密码都有一个template
    private SubChar subChar = new SubChar();//内部维护了hashmap存放用户输入的密码字串
    private MaskGenerator maskGenerator = null;


    public static void main(String[] args) throws IOException {
        HashCatView hashCatView = new HashCatView();
        hashCatView.mainPage();
    }

    public void mainPage() throws IOException {
        while(loop){
            System.out.println("======Welcome to the PPRT tool======");
            System.out.println("\t\t 1 Recover password");
            System.out.println("\t\t 2 Quit");
            System.out.print("Please enter your choice: ");
            key = Utility.readString(1);
            switch (key){
                case "1":
                    //破解程序
                    while(loop){
                        System.out.println("\n======Please select the operation you want======");
                        System.out.println("\t\t 1 Enter the length of your password");
                        System.out.println("\t\t 2 Enter password features");
                        System.out.println("\t\t 3 Adding a password substring");
                        System.out.println("\t\t 4 Creating a password mask");
                        System.out.println("\t\t 5 Recover");
                        System.out.println("\t\t 0 Quit");
                        System.out.print("Please enter your choice: ");
                        key = Utility.readString(1);
                        switch (key){
                            case "1":
                                certainty.inputPwdLength();
                                break;
                            case "2":
                                setPwdFeature();
                                break;
                            case "3":
                                subChar.acquireSubChar();
                                break;
                            case "4":
                                generateMasks();
                                break;
                            case "5":
                                writeMaskFile();
                                break;
                            case "0":
                                loop = false;
                                break;
                            default:
                                System.out.println("You have made an error in your input, please re-enter.");
                                break;
                        }
                    }
                    break;
                case "2":
                    loop = false;
                    break;
                default:
                    System.out.println("You have made an error in your input, please re-enter.");
            }
        }
        System.out.println("You have quited");
    }

    private void generateMasks() {
        HashMap<Integer, Integer> pwdLengthAndCertainty = certainty.getPwdLengthAndCertainty();
        if(pwdLengthAndCertainty.size() == 0){
            System.out.println("Please enter the length of your password first!");
            return;
        }
        if (maskTemplates.size() == 0){
            System.out.println("Please set the password feature first!");
            return;
        }
        maskGenerator = new MaskGenerator(certainty, maskTemplates, subChar);
        maskGenerator.generateMasks();
        for (Mask mask : maskGenerator.getAllMasks()){
            System.out.println(mask);
        }
    }

    public void setPwdFeature(){
        HashMap<Integer, Integer> pwdLengthAndCertainty = certainty.getPwdLengthAndCertainty();
        if(pwdLengthAndCertainty.size() == 0){
            System.out.println("Please enter the length of your password first!");
            return;
        }
        for (Integer integer : pwdLengthAndCertainty.keySet()) {
            MaskTemplate maskTemplate = new MaskTemplate(integer);
            maskTemplate.setMaskFeature();
            maskTemplates.add(maskTemplate);
        }
    }

    public void writeMaskFile() throws IOException {
        if (maskGenerator == null){
            System.out.println("Please create a password mask first!");
            return;
        }
        WriteMaskByIO.writeMaskFile(maskGenerator);
        WriteMaskByIO.close();
        ExecuteHashCat.executeInPrompt();
    }
}
