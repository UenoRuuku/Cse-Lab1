package Implement;

import Interface.IBlock;
import Interface.IFile;
import Interface.IFileManager;
import Interface.IId;
import util.config;
import util.data;
import util.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.LinkedTransferQueue;

import static util.util.getFileName;


public class File implements IFile {

    FileId id = new FileId();
    FileManager fm;
    String name;

    File(IId id, FileManager fm) {
        this.id.setNum(id.getNum());
        this.id.setHash(id.getHash());
        this.fm = fm;
    }

    public void setFileManager(FileManager f) {
        fm = f;
    }

    public File(String name, int num, String filename) {
        this.id.setNum(num);
        this.id.setHash(name);
        this.name = filename;
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
            for (IBlock b : data.buffer.get(hash)){
                fileData.put(b.getIndexId().getNum(),new String(b.read()));
            }
        }
        //如果buffer中不存在
        else {
            for (BlockManager bm : data.bmList) {
                for (Block b : bm.getBlockList()) {
                    if (b.getIndexId().getHash().equals(hash)) {
                        fileData.put(b.getIndexId().getNum(), new String(b.read()));
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
        //获取元素
        BlockManager bm = data.bmList.get(random.nextInt(data.bmList.size()));
        int length = b.length / 32;
        if (b.length % 32 != 0) {
            length += 1;
        }
        int cursor = 0;
        for (int i = 0; i < length; i++) {
            byte[] bArray = new byte[32];
            if (b.length % 32 != 0 && i == length - 1) {
                bArray = new byte[b.length % 32];
            }
            for (; cursor < i * 32; cursor++) {
                bArray[cursor % 32] = b[cursor];
            }
            //写入buffer和blockMAnager
            util.buffer_write(bm.newBlock(bArray, id.getHash()), id.getHash());

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
        return 0;
    }

    @Override
    public void setSize(long newSize) {

    }
}
