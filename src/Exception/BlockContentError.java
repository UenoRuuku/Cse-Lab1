package Exception;

public class BlockContentError extends Exception {
    public BlockContentError(){
        System.out.println("block信息损坏，读取失败");
    }
}
