package Interface;

public interface IBlockManager {
    IBlock getBlock(IId indexIId);

    IBlock newBlock(byte[] b, String hash, int num);

    default IBlock newEmptyBlock(int blockSize,String hash, int num) {
        return newBlock(new byte[blockSize],hash,num);
    }
}