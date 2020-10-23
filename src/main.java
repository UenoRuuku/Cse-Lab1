import Implement.Block;
import Implement.BlockManager;
import Implement.FileManager;
import util.config;
import util.*;

import java.io.*;
import java.util.*;

import static java.lang.Integer.parseInt;
import static util.data.bmList;
import static util.data.fmList;
import static util.util.MD5;
import static util.util.getFileName;

public class main {
    public static void init() {
        System.out.println("开始初始化……");
        //初始化blockManager和fileManager
        StringBuilder sbf = new StringBuilder();
        BufferedReader reader = null;
        File self = new File(config.ManagerConfigPath);
        try{
            reader = new BufferedReader(new FileReader(self));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
        }catch (IOException e){
            System.out.println("读取Manager信息失败，将复位manager数量");
        }
        int num = parseInt(sbf.toString());


        for (int i = 0; i < num; i++) {
            BlockManager bm = new BlockManager("BM" + i);
            FileManager fm = new FileManager("FM" + i);
            bmList.add(bm);
            fmList.add(fm);
        }


        //读取block并分配给各个blockManager
        File block = new File(config.BlockPath);
        File[] tempList = block.listFiles();
        reader = null;

        try {
            for (File value : tempList) {
                sbf = new StringBuilder();
                if (value.isFile()) {
                    String fileName = value.getName();
                    if (util.checkSuffix(fileName).equals(".meta")) {
                        try {
                            reader = new BufferedReader(new FileReader(value));
                            String tempStr;
                            while ((tempStr = reader.readLine()) != null) {
                                sbf.append(tempStr);
                                sbf.append("\n");
                            }
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("读取block文件时发生错误，初始化失败");
                        }
                        HashMap<String, String> inf = util.getMetaInfBlock(sbf.toString());
                        //生成保存在blockManager中的block
                        Block blo = new Block(getFileName(fileName), parseInt(inf.get("size")), inf.get("hash"), inf.get("check"), parseInt(inf.get("order")));
                        Random r = new Random();
                        int a = r.nextInt(bmList.size());
                        bmList.get(a).addBlock(blo);
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println("<!> block文件夹为空");
        }


        //读取文件并分配给各个fileManager
        File file = new File(config.FilePath);
        File[] fileList = file.listFiles();
        BufferedReader reader2 = null;
        StringBuffer sbf2;

        try {
            for (File value : fileList) {
                sbf2 = new StringBuffer();
                if (value.isFile()) {
                    String fileName = value.getName();
                    if (util.checkSuffix(fileName).equals(".meta")) {
                        try {
                            reader2 = new BufferedReader(new FileReader(value));
                            String tempStr;
                            while ((tempStr = reader2.readLine()) != null) {
                                sbf2.append(tempStr);
                                sbf2.append("\n");
                            }
                            reader2.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("读取file文件时发生错误，初始化失败");
                        }
                        HashMap<String, String[]> inf = util.getMetaInfFile(sbf2.toString());
//                        System.out.println(fileName);
//                        for(String s : inf.get("hash")){
//                            System.out.println(s);
//                        }
                        Implement.File f = new Implement.File(inf.get("hash"), inf.get("logic") ,parseInt(inf.get("size")[0]), util.getFileName(fileName));
                        int i = parseInt(inf.get("fm")[0]);
                        f.setFileManager(fmList.get(i));
                        fmList.get(i).addFile(f);
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println("<!> file文件夹为空");
        }
        System.out.println("初始化结束……");
    }

    public static void mainCircle() {
        System.out.println("文件管理系统运行中");
        for (FileManager fm : fmList) {
            System.out.println(fm.name + ":");
            for (Implement.File f : fm.getFileList()) {
                System.out.println(f.getName());
            }
            System.out.println();
        }

        for (BlockManager fm : bmList) {
            System.out.println(fm.getName() + ":");
            for (Implement.Block f : fm.getBlockList()) {
                System.out.println(f.getName());
            }
            System.out.println();
        }


    }




    public static void main(String[] args) {
        init();
//        test.readTest();
        while(true){
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入指令0-6");
            System.out.println("0:新建文件，1:新建fileManager，2:读取文件，" +
                    "3:复制文件，4:读取块，5:修改文件，6:退出系统");
            int i = sc.nextInt();
            switch (i){
                case 0:
                    controller.newFile();
                    break;
                case 1:
                    controller.newFileManager();
                    break;
                case 2:
                    controller.smart_cat();
                    break;
                case 3:
                    controller.smart_copy();
                    break;
                case 4:
                    controller.smart_hex();
                    break;
                case 5:
                    controller.smart_write();
                    break;
                case 6:
                    break;
                default:
                    System.out.println("未知指令");
            }
            if(i == 7){
                System.out.println("系统退出，再见");
                break;
            }
        }


    }
}
