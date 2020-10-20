package Interface;

public interface IFile {
    int MOVE_CURR = 0;
    int MOVE_HEAD = 1;
    int MOVE_TAIL = 2;

    IId getFileId();

    IFileManager getFileManager();

    byte[] read(int length);

    void write(byte[] b);

    default long pos() {
        return move(0, MOVE_CURR);
    }

    long move(long offset, int where);

    //使⽤buffer的同学需要实现
    void close();

    long size();

    void setSize(long newSize);
}