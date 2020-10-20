package Interface;

public interface IBlockManager {
    IBlock getBlock(IId indexIId);

    IBlock newBlock(byte[] b, String hash);

    default IBlock newEmptyBlock(int blockSize,String hash) {
        return newBlock(new byte[blockSize],hash);
    }
}