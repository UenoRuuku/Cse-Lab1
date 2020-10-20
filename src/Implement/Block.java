package Implement;

import Interface.IBlock;
import Interface.IBlockManager;
import Interface.IId;
import util.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

public class Block implements IBlock {

    private int size = 0;
    private byte[] b = null;
    private BlockManager bm;
    private BlockId id = new BlockId();
    public String name = "";
    //最大块存储32B
    public static final int maxSize = 32;

    //初始化时新建内存中的空block
    public Block(String name, int size, String hash,String check,int num) {
        this.name = name;
        this.size = size;
        id.setCheck(check);
        id.setHash(hash);
        id.setNum(num);
        this.read();
    }

    public void setB(byte[] b){
        this.b = b;
    }

    public void setBlockManager(BlockManager bm){
        this.bm = bm;
    }

//    Block ()

    //新建block，将内容缓存在内存中
    Block(byte[] b, BlockManager bm, int num, String hash) {
        this.size = b.length;
        this.b = b;
        this.bm = bm;
        this.name = UUID.randomUUID().toString();
        id.setOrder(num);
        id.setHash(hash);
        id.setCheck(util.util.MD5(b.toString()));
    }

    @Override
    public IId getIndexId() {
        return id;
    }

    @Override
    public IBlockManager getBlockManager() {
        return bm;
    }

    @Override
    public byte[] read() {
        if (b != null) {
            System.out.println("a");
            return b;
        }
        //todo:从物理内存中读取数据
        else{
            StringBuilder sbf = new StringBuilder();
            BufferedReader reader = null;
            File self = new File(config.BlockPath+"/"+name+".data");
            try{
                reader = new BufferedReader(new FileReader(self));
                String tempStr;
                while ((tempStr = reader.readLine()) != null) {
                    sbf.append(tempStr);
                    sbf.append("\n");
                }
                reader.close();
            }catch (IOException e){
                System.out.println("读取块失败");
            }
            return sbf.toString().getBytes();
        }
    }

    @Override
    public int blockSize() {
        return size;
    }

    public void setSize(int size){
        this.size = size;
    }
}
