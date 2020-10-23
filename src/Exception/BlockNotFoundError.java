package Exception;

import Implement.Block;

public class BlockNotFoundError extends Exception {
    public BlockNotFoundError(int i){
        System.out.println("BlockNotFoundError: 在读取第"+i+"个数据块时遭遇错误,尝试读取备份");
    }
    public BlockNotFoundError(){
        System.out.println("BlockNotFoundError: 没有找到指定的块，请确认输入名称无误");
    }
}
