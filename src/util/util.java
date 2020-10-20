package util;

import Implement.Block;
import Implement.BlockManager;
import Interface.IBlock;

import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class util {

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

    public static HashMap<String, String> getMetaInfFile(String i){
        HashMap<String,String> ret = new HashMap<>();
        String[] a = i.split("\n");
        ret.put("size",a[0]);
        ret.put("hash", a[1]);
        return ret;
    }

    public static void buffer_write(IBlock b , String a) {
        if(!data.buffer.containsKey(a)){
            data.buffer.put(a,new ArrayList<>());
        }
        data.buffer.get(a).add(b);
        //todo:将写入物理内存
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
