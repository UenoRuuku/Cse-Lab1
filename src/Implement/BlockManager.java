package Implement;

import Interface.IBlock;
import Interface.IBlockManager;
import Interface.IId;
import util.config;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//用于保存blockMeta的所有信息
public class BlockManager implements IBlockManager {

    private List<Block> blockList = new ArrayList<>();
    String name = "";

    public String getName(){
        return name;
    }

    public void addBlock(Block b) {
        b.setBlockManager(this);
        blockList.add(b);
    }

    public List<Block> getBlockList() {
        return blockList;
    }

    public BlockManager(String name){
        this.name = name;
    }

    @Override
    public IBlock getBlock(IId indexIId) {
        return blockList.get(indexIId.getNum());
    }


    //这个函数应该只有再调用buffer_write之后才会调用
    @Override
    public IBlock newBlock(byte[] b, String hash, int num) {
        //此处传入的数组长度应必然小于32
        assert b.length < 32;
        Block add = new Block(b, this, num, hash);
        add.name = UUID.randomUUID().toString();
        add.setSize(b.length);
        java.io.File file = new java.io.File(config.BlockPath+"/"+add.getName()+".meta");  //创建文件对象
        try {
            if (!file.exists()) {				//如果文件不存在则新建文件
                file.createNewFile();
            }
            FileOutputStream output = new FileOutputStream(file);

            String s = add.blockSize() + "\n" +
                    util.util.MD5(new String(b)) + "\n" +
                    add.getIndexId().getHash() + "\n" +
                    add.getIndexId().getNum();
            output.write(s.getBytes());				//将数组的信息写入文件中

            output.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("写入失败");
        }
        blockList.add(add);
        return add;
    }

    @Override
    public IBlock newEmptyBlock(int blockSize, String hash, int num) {
        return newBlock(new byte[blockSize], hash, num);
    }
}
