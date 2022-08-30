package com.harry.domain;

import com.harry.utilities.Utility;

import java.util.ArrayList;

public class MaskTemplate {
    private Mask template;
    private ArrayList<String> keyForDict = new ArrayList<>();
    private PlaceHolder placeHolder = new PlaceHolder();
    private int headLength;
    private int tailLength;
    private int remainLength;

    public MaskTemplate() {
    }

    public MaskTemplate(int length) {
        this.template = new Mask(length);
        this.remainLength = length;
    }

    public int getTemplateLength() {
        return template.getLength();
    }

    public int getHeadLength() {
        return headLength;
    }

    public int getTailLength() {
        return tailLength;
    }

    public String[] getMaskTemplateContent(){
        return template.getMaskContent();
    }
    //返回template的copy，避免修改template
    public String[] getCopyOfMTContent(){
        String[] temp = template.getMaskContent();
        String[] copy = new String[temp.length];
        for (int i = 0; i < temp.length; i++) {
            copy[i] = temp[i];
        }
        return copy;
    }

    public void setMaskFeature(){
        String[] maskContent = template.getMaskContent();
        decidePwdCombination();
        boolean headFlag = false;
        boolean tailFlag = false;
        loop:
        while (true){
            System.out.println("Please select the password position (0 to exit): ");
            System.out.println("\t\t 1 Head");
            System.out.println("\t\t 2 Tail");
            String key = Utility.readString(1);
            switch (key){
                case "1":
                    if (headFlag){
                        System.out.println("You have set the head feature, please select another position.");
                        break;
                    }
                    setTemplateHeadOrTail(maskContent, 0);
                    headFlag = true;
                    break;
                case "2":
                    if (tailFlag){
                        System.out.println("You have set the tail feature, please select another position.");
                        break;
                    }
                    setTemplateHeadOrTail(maskContent, template.getLength()-1);
                    tailFlag = true;
                    break;
                case "0":
                    break loop;
                default:
                    System.out.println("You have made an error in your input, please re-enter.");
                    break;
            }
        }
        String restPlaceHolder = placeHolder.getPlaceHolder(keyForDict);
        for(int i = 0; i<template.getLength(); i++){
            if(maskContent[i] == null){
                maskContent[i] = restPlaceHolder;
            }
        }
        System.out.println(template);

    }

    private void setTemplateHeadOrTail(String[] maskContent, int index){
        System.out.println("Please select the character type (0 to exit): ");
        System.out.println("\t\t 1 Numeric");
        System.out.println("\t\t 2 Uppercase");
        System.out.println("\t\t 3 Lowercase");
        System.out.println("\t\t 4 Special");
        String type = Utility.readString(1);
//        System.out.println("请输入字符长度： ");
//        int length = Utility.readInt(0);
        switch (type){
            case "1":
                insertHeadAndTail(maskContent, index, "Numeric");
                break;
            case "2":
                insertHeadAndTail(maskContent, index, "Uppercase");
                break;
            case "3":
                insertHeadAndTail(maskContent, index, "Lowercase");
                break;
            case "4":
                insertHeadAndTail(maskContent, index, "Special");
                break;
            case "0":
                break;
            default:
                System.out.println("You have made an error in your input, please re-enter.");
                break;
        }
    }

    private void insertHeadAndTail(String[] maskContent, int index, String type){
        int length = 0;
        while (true) {
            System.out.println("Please enter the length of characters: ");
            length = Utility.readInt();
            if (length < 0){
                System.out.println("The length of the character cannot be less than 0. Please re-enter it.");
                continue;
            }
            if(length > maskContent.length){
                System.out.println("The length of the character is greater than the length of the password, please re-enter it.");
                continue;
            }
            if(length > remainLength){
                System.out.println("The length of the character is greater than the remaining password length, please re-enter.");
                continue;
            }
            break;
        }
        //index=0表示开头
        if (index == 0) {
            for (;index < length; index++ ) {
                maskContent[index] = PlaceHolder.getDict().get(type);
            }
            headLength = length;
            remainLength -= length;
        }else{
            for (int i = index; i > index - length; i--){
                maskContent[i] = PlaceHolder.getDict().get(type);
            }
            tailLength = length;
            remainLength -= length;
        }
    }

    private void decidePwdCombination(){
        //判断用户密码总体是否有固定结构
        System.out.println("Does the "+template.getLength()+" characters password consist of specific characters, such as lowercase letters plus numbers?");
        char confirmSelection = Utility.readConfirmSelection();
        if (confirmSelection == 'Y'){
            loopForCombination:
            while(true){
                System.out.println("Please select the password composition (0 to exit): ");
                System.out.println("\t\t 1 Numeric");
                System.out.println("\t\t 2 Uppercase");
                System.out.println("\t\t 3 Lowercase");
                System.out.println("\t\t 4 Special");
                String pwdCombination = Utility.readString(1);
                switch (pwdCombination){
                    case "1":
                        if(keyForDict.contains("Numeric")){
                            System.out.println("You have already selected the character, please reselect it.");
                            continue;
                        }
                        keyForDict.add("Numeric");
                        break;
                    case "2":
                        if(keyForDict.contains("Uppercase")){
                            System.out.println("You have already selected the character, please reselect it.");
                            continue;
                        }
                        keyForDict.add("Uppercase");
                        break;
                    case "3":
                        if(keyForDict.contains("Lowercase")){
                            System.out.println("You have already selected the character, please reselect it.");
                            continue;
                        }
                        keyForDict.add("Lowercase");
                        break;
                    case "4":
                        if(keyForDict.contains("Special")){
                            System.out.println("You have already selected the character, please reselect it.");
                            continue;
                        }
                        keyForDict.add("Special");
                        break;
                    case "0":
                        break loopForCombination;
                    default:
                        System.out.println("You have made an error in your input, please re-enter.");
                        break;
                }
            }
        }
    }
}
