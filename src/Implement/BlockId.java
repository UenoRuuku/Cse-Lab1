package Implement;

import Interface.IId;

public class BlockId implements IId {
    //File name
    String hash;
    //order in file
    int num;
    //check if the block work in good condition
    String check;
    //how large the block is (no more than 32)
    int size;
    // don't write into block.data, which refers to the order in blockManager
    int order = 0;

    public void setOrder(int i) {
        order = i;
    }

    public int getOrder() {
        return order;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String setCheck(String c) {
        this.check = c;
        return this.check;
    }

    public String getCheck() {
        return check;
    }

    @Override
    public int getNum() {
        return num;
    }

    @Override
    public String getHash() {
        return hash;
    }
}
