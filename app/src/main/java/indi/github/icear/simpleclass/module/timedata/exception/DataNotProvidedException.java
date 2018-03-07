package indi.github.icear.simpleclass.module.timedata.exception;

/**
 * Created by icear on 2018/3/7.
 * DataNotProvidedException 数据中未找到目标的相关信息
 */
public class DataNotProvidedException extends Exception {
    public DataNotProvidedException(String v) {
        super(v);
    }
}
