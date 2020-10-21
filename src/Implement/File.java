package Implement;

import Interface.IBlock;
import Interface.IFile;
import Interface.IFileManager;
import Interface.IId;
import util.config;
import util.data;
import util.util;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedTransferQueue;

import static util.util.getFileName;


public class File implements IFile {

    FileId id = new FileId();
    FileManager fm;
    String name;
    List<String> logicBlock = new ArrayList<>();
    List<String> hashes = new ArrayList<>();


    File(IId id, FileManager fm) {
        this.id.setNum(id.getNum());
        this.id.setHash(id.getHash());
        this.fm = fm;
    }

    public void addLogicBlock(String s) {
        logicBlock.add(s);
    }

    public void setFileManager(FileManager f) {
        fm = f;
    }

    public File(String[] hash, int num, String filename) {
        this.id.setNum(num);
        this.id.setHash(hash[0]);
        this.name = filename;
        Collections.addAll(hashes, hash);
    }

    public File(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public IId getFileId() {
        return id;
    }

    @Override
    public IFileManager getFileManager() {
        return fm;
    }

    @Override
    public byte[] read(int length) {
        return new byte[0];
    }

    public byte[] read() {
        String hash = id.getHash();
        HashMap<Integer, String> fileData = new HashMap<>();
        byte[] ret;

        //如果已在buffer中存在
        if (data.buffer.containsKey(hash)) {
            for (String s : hashes) {
                IBlock b = data.buffer.get(s);
                fileData.put(b.getIndexId().getNum(), new String(b.read()));
            }
        }
        //如果buffer中不存在
        else {
            for(String s: hashes){
                for (BlockManager bm : data.bmList) {
                    for (Block b : bm.getBlockList()) {
                        if (b.getIndexId().getHash().equals(s)) {
                            if (b.getIndexId().getNum() * 32 >= id.getNum()) {
                                break;
                            }
                            fileData.put(b.getIndexId().getNum(), new String(b.read()));
                        }
                    }
                }
            }

        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fileData.size(); i++) {
            sb.append(fileData.get(i));
        }
        ret = sb.toString().getBytes();
        return ret;
    }


    @Override
    public void write(byte[] b) {
        Random random = new Random();
        id.setNum(b.length);
        //获取元素
        BlockManager bm = data.bmList.get(random.nextInt(data.bmList.size()));

        int length = b.length / 32;
        if (b.length % 32 != 0) {
            length += 1;
        }
        int cursor = 0;
        for (int i = 0; i < length; i++) {
            byte[] bArray = new byte[32];
            String code = UUID.randomUUID().toString();
            hashes.add(code);
            if (b.length % 32 != 0 && i == length - 1) {
                bArray = new byte[b.length % 32];
            }
            for (; cursor < i * 32 + bArray.length; cursor++) {
                bArray[cursor % 32] = b[cursor];
            }
            //写入buffer和blockMAnager
            System.out.println(i);
            util.buffer_write(bm.newBlock(bArray, code, i), code);
        }


        java.io.File file = new java.io.File(config.FilePath + "/" + name + ".meta");  //创建文件对象
        try {
            if (!file.exists()) {                //如果文件不存在则新建文件
                file.createNewFile();
            }
            FileOutputStream output = new FileOutputStream(file);

            StringBuilder s = new StringBuilder(id.getNum() + "\n");

            for (String s1 : hashes) {
                s.append(s1);
                s.append("\n");
            }

            output.write(s.toString().getBytes());//将数组的信息写入文件中

            output.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("写入失败");
        }
    }

    @Override
    public long pos() {
        return 0;
    }

    @Override
    public long move(long offset, int where) {
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public long size() {
        return id.getNum();
    }

    @Override
    public void setSize(long newSize) {

    }
}
