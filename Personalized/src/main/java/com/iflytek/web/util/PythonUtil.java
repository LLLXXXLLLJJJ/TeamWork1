package com.iflytek.web.util;

import java.io.DataInputStream;
import java.io.InputStream;

public class PythonUtil {

    public static final String PERSONAL_RECOMMENDATION_FIILE = "python/personal_recommendation_func.py";
    public static final String ALSOBUY_RECOMMENDATION_FIILE = "python/personal_recommendation_func.py";

    public static void executePython(Integer userID, String command){
        try {
            // 进行一个python的调用
            String exe = "python";
            String path = PythonUtil.class.getResource("/").getPath();
            path = path.substring(1, path.length());
            command = path + command;
            // String command = "D:\\teacher\\javaee\\project\\bookstore\\personal_recommendation_func.py";
            String userId = String.valueOf(userID);
//            String[] cmdArr = new String[]{exe, command, userId};
            String cmd = "python " + command + " "+userId;
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream is = process.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            String str = dis.readLine();
            int status = process.waitFor();
            if (status == 0) {
                System.out.println("操作成功");
            }
            else {
                System.out.println("操作失败");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        Integer userId = 12;
//        String command = "D:\\teacher\\javaee\\project\\bookstore\\personal_lbr.py";
//        executePython(userId, command);

        executePython(11, PERSONAL_RECOMMENDATION_FIILE);
    }
}
