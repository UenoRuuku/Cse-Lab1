package util;

import Implement.File;
import Implement.FileManager;
import Interface.IId;

import java.io.RandomAccessFile;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import static util.data.fmList;

public class test {
    // passed
    public static void readTest(){
        Scanner sc = new Scanner(System.in);
        int i = sc.nextInt();
        FileManager a = fmList.get(i);
        String name = sc.next();
        for(Implement.File f : a.getFileList()){
            if(f.getName().equals(name)){
                System.out.println("content: "+new String(f.read()));
            }
        }
    }

    public static void writeTest(){
        Scanner sc = new Scanner(System.in);
        String i = sc.next();
        String j = sc.next();
        File f = new File(i);
        Random r = new Random();
        int n = r.nextInt(fmList.size());
        f.setFileManager(fmList.get(n));
        fmList.get(n).addFile(f);
        f.write(j.getBytes());
    }


}
