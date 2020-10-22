package util;

import Implement.Block;
import Implement.BlockManager;
import Interface.IBlock;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class util {


    public static byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }


    public static String MD5(String str){
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest  = md5.digest(str.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        return new BigInteger(1, digest).toString(16);
    }

    public static String checkSuffix(String originalFilename) {
        int lastIndexOf = originalFilename.lastIndexOf(".");
        //获取文件的后缀名
        return originalFilename.substring(lastIndexOf);
    }

    public static String getFileName(String filename){
        return filename.replaceAll("[.][^.]+$", "");
    }

    public static HashMap<String, String> getMetaInfBlock(String i){
        HashMap<String,String> ret = new HashMap<>();
        String[] a = i.split("\n");
        ret.put("size",a[0]);
        ret.put("check", a[1]);
        ret.put("hash", a[2]);
        ret.put("order",a[3]);
        return ret;
    }

    public static HashMap<String, String[]> getMetaInfFile(String i){
        HashMap<String,String[]> ret = new HashMap<>();
        String[] a = i.split("\n");
        ret.put("size",new String[]{a[0]});
        String[] hashes = new String[a.length-1];
        System.arraycopy(a, 1, hashes, 0, a.length - 1);
        String[] h1 = new String[a.length-1];
        String[] h2 = new String[a.length-1];
        for(int p = 0; p < hashes.length ; p++){
            String[] temp = hashes[p].split(",");
            h1[p] = temp[0];
            h2[p] = temp[1];
        }
        ret.put("hash", h1);
        ret.put("logic",h2);
        return ret;
    }

    public static void buffer_write(IBlock add , String a) {
        data.buffer.put(a,add);
        //todo:将写入物理内存
        java.io.File file = new java.io.File(config.BlockPath+"/"+add.getName()+".data");  //创建文件对象
        try {
            if (!file.exists()) {				//如果文件不存在则新建文件
                file.createNewFile();
            }
            FileOutputStream output = new FileOutputStream(file);

            output.write(add.read());				//将数组的信息写入文件中

            output.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("写入失败");
        }
    }

    public static void smart_cat() {

    }

    public static void smart_hex() {
    }

    public static void smart_write() {
    }

    public static void smart_copy() {
    }
}
