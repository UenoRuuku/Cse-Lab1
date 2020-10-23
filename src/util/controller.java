package util;

import Implement.Block;
import Implement.BlockManager;
import Implement.File;
import Implement.FileManager;
import Interface.IFile;
import Exception.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import static util.data.bmList;
import static util.data.fmList;

public class controller {
    public static void smart_cat() {
        //TODO:
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入需要访问的filemanager(0-"+(fmList.size()-1)+")");
        int num = sc.nextInt();
        FileManager fm = fmList.get(num);
        System.out.println(fm.name + ":");
        for (Implement.File f : fm.getFileList()) {
            System.out.println(f.getName());
        }
        System.out.println();
        System.out.println("请输入需要访问的文件名");
        String name = sc.next();
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
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入需要访问的blockmanager(0-2)");
        int num = sc.nextInt();
        BlockManager bm = bmList.get(num);
        System.out.println(bm.getName() + ":");
        for (int i = 0 ; i < bm.getBlockList().size(); i++) {
            System.out.println(i+":"+bm.getBlockList().get(i).getName());
        }
        System.out.println("请输入需要访问的块次序");
        int name = sc.nextInt();
        Block b = bm.find(bm.getBlockList().get(name).getIndexId().getHash());
        try{
            if(b == null){
                throw new BlockNotFoundError();
            }
        }catch (BlockNotFoundError e){
            return;
        }
        System.out.println(util.str2HexStr(new String(b.read())));

    }

    public static void smart_write() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入需要访问的filemanager(0-"+(fmList.size()-1)+")");
        int num = sc.nextInt();
        FileManager fm = fmList.get(num);
        System.out.println(fm.name + ":");
        for (Implement.File f : fm.getFileList()) {
            System.out.println(f.getName());
        }
        System.out.println();
        System.out.println("请输入需要访问的文件名");
        String name = sc.next();
        for(Implement.File file : fm.getFileList()){
            if(file.getName().equals(name)){
                System.out.println("原始指针位置"+file.pos());
                System.out.println("文件大小"+file.size());
                System.out.println("请输入指针调整方式 0=从当前位置向后偏移，1=从头开始偏移，2=从尾部向前偏移");
                int type = sc.nextInt();
                System.out.println("偏移量？");
                int offset = sc.nextInt();
                file.move(offset,type);
                System.out.println("要改成什么？");
                String input = sc.next();
                try{
                    if(file.pos()>file.size()){
                        throw new PointerOutofRangeError();
                    }
                }catch (PointerOutofRangeError e){
                    file.move(0, IFile.MOVE_HEAD );
                    return;
                }
                file.write(input.getBytes());
                return;
            }
        }
        System.out.println("错误的文件名");
    }

    public static void smart_copy() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入需要访问的filemanager(0-"+(fmList.size()-1)+")");
        int num = sc.nextInt();
        FileManager fm = fmList.get(num);
        System.out.println(fm.name + ":");
        for (Implement.File f : fm.getFileList()) {
            System.out.println(f.getName());
        }
        System.out.println();
        System.out.println("请输入需要访问的文件名");
        String name = sc.next();
        for(Implement.File file : fm.getFileList()){
            if(file.getName().equals(name)){
                System.out.println("要复制到哪个fileManager?(0-"+(fmList.size()-1)+")?");
                int which = sc.nextInt();
                System.out.println("文件名？");
                String newName = sc.next();
                File f = new File(newName);
                byte[] temp = file.read();
                fmList.get(which).newFile(f,temp);
                System.out.println("复制成功");
                return;
            }
        }
        System.out.println("错误的文件名");
    }

    public static void newFileManager(){
        FileManager m = new FileManager("FM"+fmList.size());
        java.io.File file = new java.io.File(config.ManagerConfigPath);  //创建文件对象
        fmList.add(m);
        try {
            if (!file.exists()) {				//如果文件不存在则新建文件
                file.createNewFile();
            }
            FileOutputStream output = new FileOutputStream(file);

            String s = String.valueOf(fmList.size());
            output.write(s.getBytes());				//将数组的信息写入文件中

            output.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("新建fileManager失败");
            return;
        }
        System.out.println("新建fileManager:"+m.name+"成功，共"+fmList.size()+"个");
    }

    public static void newFile(){
        Scanner sc = new Scanner(System.in);
        System.out.println("要在哪个文件夹新建");
        int n = sc.nextInt();
        try{
            if(n>=fmList.size()){
                throw new PointerOutofRangeError(n);
            }
        } catch (PointerOutofRangeError pointerOutofRangeError) {
            System.out.println("新建文件失败");
            return;
        }
        System.out.println("文件名？");
        String i = sc.next();
        System.out.println("内容？");
        String j = sc.next();
        File f = new File(i);
        fmList.get(n).newFile(f,j.getBytes());
        System.out.println("新建文件成功");
    }

}
