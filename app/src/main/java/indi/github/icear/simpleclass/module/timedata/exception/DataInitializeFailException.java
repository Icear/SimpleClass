package indi.github.icear.simpleclass.module.timedata.exception;

/**
 * Created by icear on 2018/3/7.
 * DataInitializeFailException 数据初始化失败
 */
public class DataInitializeFailException extends RuntimeException {
    public DataInitializeFailException(String v) {
        super(v);
    }
}
