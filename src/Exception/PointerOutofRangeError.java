package Exception;

import static util.data.fmList;

public class PointerOutofRangeError extends Exception {
    public PointerOutofRangeError(){
        System.out.println("PointerOutofRangeError: 错误的指针输入，指针已复位");
    }

    public PointerOutofRangeError(int n){
        System.out.println("PointerOutofRangeError: "+"无该fileManager，请求"+n+",实际最大"+(fmList.size()-1));
    }
}
