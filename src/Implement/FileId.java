package Implement;

import Interface.IId;

public class FileId implements IId {
    //FileName
    String hash;
    //FileSize
    int num;

    @Override
    public int getNum() {
        return num;
    }

    @Override
    public String getHash() {
        return hash;
    }

    public void setNum(int num){
        this.num = num;
    }

    public void setHash(String code){
        this.hash = code;
    }
}
