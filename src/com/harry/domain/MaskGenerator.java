package com.harry.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

public class MaskGenerator {
    private Certainty certainty;
    private ArrayList<MaskTemplate> maskTemplates;
    private SubChar subChar;
    private ArrayList<Mask> allMasks = new ArrayList<>();
    private HashMap<Integer, ArrayList<Mask>> masksOfEveryLength = new HashMap<>();


    public MaskGenerator() {
    }

    public MaskGenerator(Certainty certainty, ArrayList<MaskTemplate> maskTemplates, SubChar subChar) {
        this.certainty = certainty;
        this.maskTemplates = maskTemplates;
        this.subChar = subChar;
    }

    public ArrayList<Mask> getAllMasks() {
        return allMasks;
    }

    public void generateMasks(){
        Set<Integer> pwdLengths = certainty.getPwdLengthAndCertainty().keySet();
        for(MaskTemplate maskTemplate : maskTemplates){
            generateMask(maskTemplate);
            if(!masksOfEveryLength.containsKey(maskTemplate.getTemplateLength())){
                System.out.println("Please re-enter the password substring with appropriate length.");
                return;
            }
        }
        sortMaskByLengthCertainty(pwdLengths);
    }

    private void generateMask(MaskTemplate maskTemplate){
        //记录剩余数量是否够其他字串放入
        int remainLength = maskTemplate.getTemplateLength();
        //建立ArrayList用于存放该长度密码的所有mask，最后放入Hashmap中
        ArrayList<Mask> masksOfThisLength = new ArrayList<>();

        HashMap<String, ArrayList<String>> userInfo = subChar.getUserInfo();
        String[] maskTemplateContent = maskTemplate.getMaskTemplateContent();
        //填充MaskTemplate的头部和尾部
        if (userInfo.containsKey("Head")){
            String head = userInfo.get("Head").get(0);
            //当字串长度大于剩余可放入长度时
            if (head.length() > remainLength){
                System.out.println("The substring you entered is too long.");
                return;
            }
            for(int i = 0; i<head.length(); i++){
                maskTemplateContent[i] = head.substring(i, i+1);
            }
            remainLength -= Math.max(head.length(), maskTemplate.getHeadLength());
        }else{
            remainLength -= maskTemplate.getHeadLength();
        }

        if (userInfo.containsKey("Tail")){
            String tail = userInfo.get("Tail").get(0);
            if (tail.length() > remainLength){
                System.out.println("The substring you entered is too long.");
                return;
            }
            int index = maskTemplateContent.length - tail.length();
            for(int i = index; i < maskTemplateContent.length; i++){
                maskTemplateContent[i] = tail.substring(i-index, i-index+1);
            }
            remainLength -= Math.max(tail.length(), maskTemplate.getTailLength());
        }else {
            remainLength -= maskTemplate.getTailLength();
        }

        //用来存放插入middle字符后的每一个mask
        ArrayList<Mask> tempMasks = new ArrayList<>();

        if (userInfo.containsKey("Middle")){
            String middle = userInfo.get("Middle").get(0);
            String certainty = userInfo.get("Middle").get(1);
            if (middle.length() > remainLength){//中间字符与头尾相差一个字符
                System.out.println("The substring you entered is too long.");
                return;
            }
            //获取插入middle字符的第一个字符在template中的索引
            int index;
            if (userInfo.containsKey("Head")){
                index = Math.max(userInfo.get("Head").get(0).length(), maskTemplate.getHeadLength());
            }else {
                index = maskTemplate.getHeadLength();
            }
            //排列组合可能性有remainLength - 2 - middle.length() + 1    -2是不包含头部后一个字符位置和尾部前一个字符位置的情况
            for (int i = index; i < remainLength-middle.length()+1 + index; i++){
                int insertIndex = i;
                String[] copyOfMTContent = maskTemplate.getCopyOfMTContent();
                for (int j = 0; j < middle.length(); j++){
                    copyOfMTContent[insertIndex++] = middle.substring(j, j+1);
                }
                tempMasks.add(new Mask(copyOfMTContent));
            }
            //将tempMasks按可能性移到masksOfThisLength中
            sortMaskByMiddleCertainty(certainty, tempMasks, masksOfThisLength);
            masksOfEveryLength.put(maskTemplate.getTemplateLength(), masksOfThisLength);
        }else{
            //没有中间字符的情况
            masksOfThisLength.add(new Mask(maskTemplateContent));
            masksOfEveryLength.put(maskTemplate.getTemplateLength(), masksOfThisLength);
        }

    }

    private void sortMaskByMiddleCertainty(String certainty, ArrayList<Mask> tempMasks, ArrayList<Mask> masksOfThisLength){
        switch (certainty){
            case "Low":
                ArrayList<Mask> reverseList = new ArrayList<>();
                while (!tempMasks.isEmpty()){
                    //永远先取中间的mask，这样中间高可能性的mask就会放在前面
                    int index = tempMasks.size()/2;
                    reverseList.add(tempMasks.get(index));
                    tempMasks.remove(index);
                }
                for(int i = reverseList.size()-1; i>=0; i--){
                    masksOfThisLength.add(reverseList.get(i));
                }
                break;
            case "High":
                //当不空时
                while (!tempMasks.isEmpty()){
                    //永远先取中间的mask，这样中间高可能性的mask就会放在前面
                    int index = tempMasks.size()/2;
                    masksOfThisLength.add(tempMasks.get(index));
                    tempMasks.remove(index);
                }
                break;
            case "Uncertain":
                masksOfThisLength.addAll(tempMasks);
                break;
        }
    }

    private void sortMaskByLengthCertainty(Set<Integer> pwdLengths){
        HashMap<Integer, Long> allDensity = new HashMap<>();
        for (Integer pwdLength : pwdLengths) {
            long density = calculateDensity(pwdLength);
//            System.out.println(density);
            //将密码长度和density以键值对形式放入allDensity
            allDensity.put(pwdLength, density);
        }
        //取出value集
        ArrayList<Long> values = new ArrayList<Long>(allDensity.values());
        //将所有density自然排序
        values.sort(Comparator.naturalOrder());
        //将masksOfEveryLength中的masks放入allMasks
        //对于按顺序存放的density
        for (Long value : values) {
            //对于所有的density
            for (Integer integer : allDensity.keySet()) {
                //如果某个长度的密码的density等于
                if (allDensity.get(integer).equals(value)){
                    //将该长度的所有mask加入allMasks中
                    allMasks.addAll(masksOfEveryLength.get(integer));
                }
            }
        }
    }

    private long calculateDensity(Integer pwdLength){
        long count = 1;
        int sizeOfMask = masksOfEveryLength.get(pwdLength).size();
        String mask = masksOfEveryLength.get(pwdLength).get(0).toString();
        if (mask.charAt(0) != '?') {
            char[] charsInMask = mask.toCharArray();
            for (int j = 0; j<charsInMask.length; j++){
                if (charsInMask[j] == '?'){
                    mask = mask.substring(j);
                    break;
                }
            }
        }
        String[] split = mask.split("\\?");
        for (String s : split) {
            String placeHolder = s;
            if (s.equals("")){
                continue;
            }
            if (s.length() > 1) {
                //当出现?acly时，取第一个字符
                placeHolder = s.substring(0, 1);
            }
            switch (placeHolder) {
                case "l":
                    count *= 26;
                    break;
                case "u":
                    count *= 26;
                    break;
                case "d":
                    count *= 10;
                    break;
                case "s":
                    count *= 33;
                    break;
                case "a":
                    count *= 95;
                    break;
                //自定义的占位符计算
                default:
                    String customMask = PlaceHolder.getDict().get(placeHolder);
                    String[] customSplit = customMask.split("\\?");
                    int num = 0;
                    for (String s1 : customSplit) {
                        if (s1.equals("")){
                            continue;
                        }
                        switch (s1){
                            case "l":
                                num += 26;
                                break;
                            case "u":
                                num += 26;
                                break;
                            case "d":
                                num += 10;
                                break;
                            case "s":
                                num += 33;
                                break;
                        }
                    }
                    count *= num;
                    break;
            }
        }
        count *= sizeOfMask;
        count /= certainty.getPwdLengthAndCertainty().get(pwdLength);
        return count;
    }
}
