package util;

import Implement.FileManager;

import java.util.Scanner;

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
                System.out.println(new String(f.read()));
            }
        }
    }

}
