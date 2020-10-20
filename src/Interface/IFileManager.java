package Interface;

public interface IFileManager {
    IFile getFile(IId fileIId);

    IFile newFile(IId fileIId);
}