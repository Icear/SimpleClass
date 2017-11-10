package indi.github.icear.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by icear on 2017/9/22.
 * 类型转换工具
 */
public class ConvertUtil {
    private static final int BUFFER_SIZE = 2048;

    /**
     * 将inputStream内容转换为String
     * @param inputStream inputStream
     * @return String结果
     * @throws IOException IOException
     */
    public static String toString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while((line = bufferedReader.readLine()) != null){
            stringBuilder.append(line);
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    /**
     * 将InputStream转换成byte数组
     * @param inputStream inputStream
     * @return byte数组
     * @throws IOException IOException
     */
    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count;
        while((count = inputStream.read(data,0,BUFFER_SIZE)) != -1)
            byteArrayOutputStream.write(data, 0, count);
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
