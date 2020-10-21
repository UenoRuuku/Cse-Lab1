package Interface;

// Block write , immutatable
public interface IBlock {
    IId getIndexId();

    IBlockManager getBlockManager();

    byte[] read();

    int blockSize();

    String getName();
}
