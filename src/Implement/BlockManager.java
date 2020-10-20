package Implement;

import Interface.IBlock;
import Interface.IBlockManager;
import Interface.IId;

import java.util.ArrayList;
import java.util.List;

//用于保存blockMeta的所有信息
public class BlockManager implements IBlockManager {

    private List<Block> blockList = new ArrayList<>();
    String name = "";


    public void addBlock(Block b) {
        b.getIndexId().setNum(blockList.size());
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

    @Override
    public IBlock newBlock(byte[] b, String hash) {
        //此处传入的数组长度应必然小于32
        assert b.length < 32;
        Block add = new Block(b, this, blockList.size(), hash);
        add.setSize(b.length);
        blockList.add(add);
        return add;
    }

    @Override
    public IBlock newEmptyBlock(int blockSize, String hash) {
        return newBlock(new byte[blockSize], hash);
    }
}
