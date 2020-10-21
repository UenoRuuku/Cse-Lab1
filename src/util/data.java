package util;

import Implement.Block;
import Implement.BlockManager;
import Implement.FileManager;
import Interface.IBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class data {
    public static List<BlockManager> bmList = new ArrayList<>();
    public static List<FileManager> fmList = new ArrayList<>();
    public static HashMap<String,IBlock> buffer = new HashMap<>();
}
