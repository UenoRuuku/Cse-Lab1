package Implement;

import Interface.IFile;
import Interface.IFileManager;
import Interface.IId;

import java.util.ArrayList;
import java.util.List;

public class FileManager implements IFileManager {
    private List<File> fileList = new ArrayList<>();
    public static final int maxSize = 50;
    public String name = "";
    public List<File> getFileList() {
        return fileList;
    }

    public FileManager(String name){
        this.name = name;
    }

    @Override
    public IFile getFile(IId fileIId) {
        return null;
    }

    @Override
    public IFile newFile(IId fileIId) {
        assert fileList.size() < maxSize;
        fileList.add(new File(fileIId,this));
        return null;
    }

    public IFile newFile(File f){
        assert fileList.size() < maxSize;
        fileList.add(f);
        return f;
    }

    public void addFile(File file){
        file.setFileManager(this);
        fileList.add(file);
    }

    FileManager(List<File> files) {
        assert files.size() < maxSize;
        fileList.addAll(files);
    }
}
