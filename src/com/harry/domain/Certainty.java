package com.harry.domain;

import com.harry.utilities.Utility;

import java.util.HashMap;

public class Certainty {
    private HashMap<Integer, Integer> pwdLengthAndCertainty = new HashMap<>();
    private int remainLengthCertainty = 100;

    public HashMap<Integer, Integer> getPwdLengthAndCertainty() {
        return pwdLengthAndCertainty;
    }
    //用户输入密码长度和可能性
    public void inputPwdLength(){
        System.out.println("Do you want to enter the length certainty manually？");
        char confirmSelection = Utility.readConfirmSelection();
        boolean flag = confirmSelection == 'Y';

        while (true) {
            System.out.println("Please enter the possible password length (-1 to exit): ");
            int length = Utility.readInt();
            //不退出
            if (length == 0 || length < -1){
                System.out.println("The length of the password cannot be less than or equal to 0. Please re-enter it.");
                continue;
            }
            if(length != -1){
                //如果密码长度已经存在
                if(pwdLengthAndCertainty.containsKey(length)){
                    System.out.println("This password length already exists, please re-enter it.");
                    continue;
                }
                //手动输入可能性
                if(flag){
                    System.out.print("Please enter the password certainty (1 - 100): ");
                    int certainty = Utility.readInt();
                    //如果可能性大于剩余可能性
                    if(certainty > remainLengthCertainty){
                        System.out.println("The sum of certainty is greater than 100, please re-enter.");
                        continue;
                    }
                    //可能性不能小于等于0
                    if(certainty <= 0){
                        System.out.println("Please enter a number greater than 0.");
                        continue;
                    }
                    pwdLengthAndCertainty.put(length, certainty);
                    remainLengthCertainty -= certainty;
                }else{
                    //否则分配的可能性全都相同
                    pwdLengthAndCertainty.put(length, 1);
                }
            }else{
                break;
            }
        }
    }
}
