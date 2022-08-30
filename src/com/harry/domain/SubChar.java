package com.harry.domain;

import com.harry.utilities.Utility;

import java.util.ArrayList;
import java.util.HashMap;

public class SubChar {
//    private MaskTemplate maskTemplate;
    private HashMap<String, ArrayList<String>> userInfo = new HashMap<>();//里面的集合存放字串，如果是中间位置，则再存放一个可能性

    public HashMap<String, ArrayList<String>> getUserInfo() {
        return userInfo;
    }

    public void acquireSubChar(){
        String subChar;
        loop:
        while (true){
            ArrayList<String> info = new ArrayList<>();
            System.out.println("Please select the position of your string in the password (0 exit): ");
            System.out.println("\t\t 1 Head");
            System.out.println("\t\t 2 Tail");
            System.out.println("\t\t 3 Middle");
            String key = Utility.readString(1);
            switch (key){
                case "1":
                    System.out.println("Please enter the password substring you remember: ");
                    subChar = Utility.readString(15);//确保字串长度不会超过密码长度
                    info.add(subChar);
                    userInfo.put("Head", info);
                    break;
                case "2":
                    System.out.println("Please enter the password substring you remember: ");
                    subChar = Utility.readString(15);//确保字串长度不会超过密码长度
                    info.add(subChar);
                    userInfo.put("Tail", info);
                    break;
                case "3":
                    System.out.println("Please enter the password substring you remember: ");
                    subChar = Utility.readString(15);//确保字串长度不会超过密码长度
                    info.add(subChar);
                    System.out.println("Please select the certainty of the middle substring (the more the substring is in the middle of the password, the higher the certainty): ");
                    System.out.println("\t\t 1 Low");
                    System.out.println("\t\t 2 High");
                    System.out.println("\t\t 3 Uncertain");
                    String middleCertainty = Utility.readString(1);
                    if (middleCertainty.equals("1")){
                        info.add("Low");
                    } else if (middleCertainty.equals("2")) {
                        info.add("High");
                    } else if (middleCertainty.equals("3")) {
                        info.add("Uncertain");
                    }else {
                        System.out.println("You have made an error in your input, please re-enter.");
                        break;
                    }
                    userInfo.put("Middle", info);
                    break;
                case "0":
                    break loop;
                default:
                    System.out.println("You have made an error in your input, please re-enter.");
                    break;
            }
        }
    }
}
