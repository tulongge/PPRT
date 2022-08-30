package com.harry.utilities;

import java.io.IOException;

public class ExecuteHashCat {
    private static Runtime runtime = Runtime.getRuntime();

    public static void executeInPrompt(){
        System.out.println("Please enter the hashcat program directory: ");
        String dir = Utility.readString(100);
        System.out.println("Please enter the hash file directory: ");
        String hashDir = Utility.readString(100);
        try {
            runtime.exec("cmd.exe /c cd \""+dir+"\" & start cmd.exe /k hashcat.exe -m 0 -a 3 \""+hashDir +"\" \""+WriteMaskByIO.getFilePath()+"\"");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
//    private Runtime runtime = Runtime.getRuntime();
//    private Process process;
//    private String[] command = new String[3];
//
//    public void setCommand(String[] command) {
//        this.command = command;
//    }
//
//    public String[] getCommand() {
//        return command;
//    }
//
//    //请求用户的hashcat文件地址
//    public void createCommand(){
//        System.out.println("Please input directory of hashcat: ");
//        String hashcatDir = Utility.readString(100);
////        hashcatDir.replace()
////        hashcatDir.replaceAll("\\\\", "\\\\\\\\"); //四个反斜杠表示一个反斜杠
//        String sysDir = hashcatDir.toCharArray()[0] + String.valueOf(hashcatDir.toCharArray()[1]);
//        System.out.println("Please input directory of hash file: ");
//        String hashDir = Utility.readString(100);
//        String maskDir = WriteMaskByIO.getFilePath();
//        command[0] = "cmd.exe";
//        command[1] = "/c";
//        command[2] = sysDir + " && cd " + hashcatDir + " && hashcat.exe -m 0 -a 3 \"" + hashDir + "\" \"" + maskDir + "\"";
//    }
//
//    public void execute(){
//        try {
//            process = runtime.exec(command);
//            String inStr = consumeInputStream(process.getInputStream());
//            String errStr = consumeInputStream(process.getErrorStream());
//            process.waitFor();
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } finally {
//            process.destroy();
//        }
//
//    }
//
//    public static String consumeInputStream(InputStream is) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(is,"GBK"));
//        String s;
//        StringBuilder sb = new StringBuilder();
//        while ((s = br.readLine()) != null) {
//            System.out.println(s);
//            sb.append(s + "\n");
//        }
//        return sb.toString();
//    }