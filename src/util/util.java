package util;

import Implement.FileManager;
import Interface.IBlock;
import Exception.*;
import Interface.IFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static util.data.fmList;


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
        ret.put("fm",new String[]{a[1]});
        String[] hashes = new String[a.length-2];
        System.arraycopy(a, 2, hashes, 0, a.length - 2);
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
        //TODO:
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入需要访问的filemanager(0-2)");
        int num = sc.nextInt();
        FileManager fm = fmList.get(num);
        System.out.println(fm.name + ":");
        for (Implement.File f : fm.getFileList()) {
            System.out.println(f.getName());
        }
        System.out.println();
        System.out.println("请输入需要访问的文件名");
        String name = sc.nextLine();
        for(Implement.File file : fm.getFileList()){
            if(file.getName().equals(name)){
                System.out.println("原始指针位置"+file.pos());
                System.out.println("文件大小"+file.size());
                System.out.println("请输入指针调整方式 0=从当前位置向后偏移，1=从头开始偏移，2=从尾部向前偏移");
                int type = sc.nextInt();
                System.out.println("偏移量？");
                int offset = sc.nextInt();
                System.out.println("长度？");
                int length = sc.nextInt();
                file.move(offset,type);
                try{
                    if(file.pos()>file.size()){
                        throw new PointerOutofRangeError();
                    }
                }catch (PointerOutofRangeError e){
                    file.move(0, IFile.MOVE_HEAD );
                }
                try{
                    if(file.pos()+length > file.size()){
                        throw new RequestTooLongError();
                    }
                }catch (RequestTooLongError e){
                    return ;
                }
                System.out.println(new String(file.read(length)));
                return;
            }
        }
        System.out.println("错误的文件名");
    }

    public static void smart_hex() {
    }

    public static void smart_write() {
    }

    public static void smart_copy() {
    }
}
