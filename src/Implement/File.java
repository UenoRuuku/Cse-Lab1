package Implement;

import Interface.IBlock;
import Interface.IFile;
import Interface.IFileManager;
import Interface.IId;
import util.config;
import util.data;
import util.util;
import Exception.*;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedTransferQueue;

import static util.data.bmList;
import static util.util.getFileName;


public class File implements IFile {

    FileId id = new FileId();
    FileManager fm;
    String name;
    List<String> logicBlock = new ArrayList<>();
    List<String> hashes = new ArrayList<>();
    long pointer = 0;


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

    public File(String[] hash, String[] logic, int num, String filename) {
        this.id.setNum(num);
        this.id.setHash(hash[0]);
        this.name = filename;
        Collections.addAll(hashes, hash);
        Collections.addAll(logicBlock, logic);
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

    public void setFm(FileManager f){
        this.fm = f;
    }

    @Override
    public byte[] read(int length) {
        byte[] r = read();
        byte[] ret = new byte[length];
        for(int ii = (int) pointer; ii < length; ii++ ){
            ret[(int) (ii-pos())] = r[ii];
        }
        return ret;
    }

    public IBlock readBlock(String hash) {
        if (data.buffer.containsKey(hash)) {
            return data.buffer.get(hash);
        } else {
            for (BlockManager bm : bmList) {
                if (bm.find(hash) != null) {
                    return bm.find(hash);
                }
            }
        }
        return null;
    }


    public byte[] read() {
        HashMap<Integer, String> fileData = new HashMap<>();
        byte[] ret;

        for (int mm = 0; mm < hashes.size(); mm++) {
            System.out.println(mm+":"+hashes.get(mm));
            byte[] temp = readBlock(hashes.get(mm)).read();
            int num = readBlock(hashes.get(mm)).getIndexId().getNum();
            if (temp == null) {
                temp = readBlock(logicBlock.get(mm)).read();
            }
            fileData.put(num, new String(temp));
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fileData.size(); i++) {
            try {
                if (fileData.get(i) == null) {
                    throw new BlockNotFoundError(i);
                } else {
                    sb.append(fileData.get(i));
                }
            } catch (BlockNotFoundError e) {
                return null;
            }
        }
        ret = sb.toString().getBytes();
        return ret;
    }


    @Override
    public void write(byte[] b) {

        long starter = pointer / Block.maxSize;
        String content = "";

        if (starter != 0) {
            String code = hashes.get((int) starter);
            for (BlockManager bm : bmList) {
                if (bm.find(code) != null) {
                    content = new String(bm.find(code).read()).substring(0, (int) pointer);
                }
            }
            id.setNum((int) (b.length+pointer));
        } else {
            id.setNum(b.length);
        }

        b = util.concat(content.getBytes(), b);

        Random random = new Random();
        //获取元素
        BlockManager bm = bmList.get(random.nextInt(bmList.size()));

        int length = b.length / 32;
        if (b.length % 32 != 0) {
            length += 1;
        }
        int cursor = 0;
        for (int i = 0; i < length; i++) {
            byte[] bArray = new byte[32];
            String code = UUID.randomUUID().toString();
            String code2 = UUID.randomUUID().toString();
            if (hashes.size() > i + (int) starter) {
                hashes.set(i + (int) starter, code);
                logicBlock.set(i+(int)starter, code2);
            } else {
                hashes.add(code);
                logicBlock.add(code2);
            }

            if (b.length % 32 != 0 && i == length - 1) {
                bArray = new byte[b.length % 32];
            }
            for (; cursor < i * 32 + bArray.length; cursor++) {
                bArray[cursor % 32] = b[cursor];
            }
            //写入buffer和blockManager
            util.buffer_write(bm.newBlock(bArray, code, i + (int) starter), code);
            util.buffer_write(bm.newBlock(bArray, code2, i + (int) starter), code2);
        }


        java.io.File file = new java.io.File(config.FilePath + "/" + name + ".meta");  //创建文件对象
        try {
            if (!file.exists()) {                //如果文件不存在则新建文件
                file.createNewFile();
            }
            FileOutputStream output = new FileOutputStream(file);

            StringBuilder s = new StringBuilder(id.getNum() + "\n");
            int m = (int) size() / Block.maxSize;
            if (size() % Block.maxSize == 0) {
                m--;
            }
            s.append(this.fm.name.charAt(2));
            s.append("\n");
            for (int i = 0; i <= m; i++) {
                s.append(hashes.get(i));
                s.append(",");
                s.append(logicBlock.get(i));
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
        return pointer;
    }

    @Override
    public long move(long offset, int where) {
        switch (where) {
            case 0 -> pointer += offset;
            case 1 -> pointer = offset;
            case 2 -> pointer = size() - 1 - offset;
        }
        try {
            if (pointer < 0 || pointer >= size()) {
                throw new PointerOutofRangeError();
            }
        } catch (PointerOutofRangeError pointerOutofRangeError) {
            pointer = 0;
            return 0;
        }
        return pointer;
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
        //新文件比旧文件大
        if (newSize >= size()) {
            //todo：找到最后一个块，并进行修改
            move(0, MOVE_TAIL);
            byte[] b = new byte[(int) (newSize - size())];
            for (byte bb : b) {
                bb = 0;
            }
            this.write(b);
        }
        //新文件比旧文件小
        else {
            //todo:找到留下的最后一个块，进行修改
            move(newSize - 1, MOVE_HEAD);
            this.write(new byte[0]);

        }
    }
}
