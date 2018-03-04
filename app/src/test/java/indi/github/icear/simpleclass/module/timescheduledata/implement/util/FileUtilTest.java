package indi.github.icear.simpleclass.module.timescheduledata.implement.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Created by icear on 2018/3/4.
 * FileUtil单元测试
 */
@RunWith(Parameterized.class)
public class FileUtilTest {
    private String originData;
    private String filteredName;

    public FileUtilTest(String originData, String filteredName) {
        this.originData = originData;
        this.filteredName = filteredName;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"2017-2018-2", "2017-2018-2"}
        });
    }

    @Test
    public void filterFileName() throws Exception {
        assertEquals(filteredName, FileUtil.filterFileName(originData));
    }

}