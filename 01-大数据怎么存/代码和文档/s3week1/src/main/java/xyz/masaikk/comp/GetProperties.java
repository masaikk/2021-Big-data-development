package xyz.masaikk.comp;

import com.amazonaws.services.s3.model.PartETag;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

public class GetProperties {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Properties pps = new Properties();
        pps.load(new FileInputStream("config.properties"));
        Enumeration enum1 = pps.propertyNames();//得到配置文件的名字
        while (enum1.hasMoreElements()) {
            String strKey = (String) enum1.nextElement();
            String strValue = pps.getProperty(strKey);
            System.out.println(strKey + "=" + strValue);
        }
    }

    public static void writeProperties(String filePath, String pKey, String pValue) throws IOException {
        Properties pps = new Properties();
        InputStream in = new FileInputStream(filePath);
        //从输入流中读取属性列表（键和元素对）
        pps.load(in);
        //调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
        //强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
        OutputStream out = new FileOutputStream(filePath);
        pps.setProperty(pKey, pValue);
        //以适合使用 load 方法加载到 Properties 表中的格式，
        //将此 Properties 表中的属性列表（键和元素对）写入输出流
        pps.store(out, "Update " + pKey + " name");
    }

    public static String getValueByKey(String filePath, String key) {
        Properties pps = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            pps.load(in);
            String value = pps.getProperty(key);
            System.out.println(key + " = " + value);
            return value;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<PartETag> getPartETagsByProp(String filePath) {
        ArrayList<PartETag> remainingPartETags = new ArrayList<PartETag>();
        try {
            for (int i = 1; ; i++) {
                String thisPartETagNumber = GetProperties.getValueByKey(filePath, "partETag" + Integer.toString(i));
//            assert thisPartETagNumber != null;
                assert thisPartETagNumber != null;
                if (thisPartETagNumber.equals("")) {
                    break;
                } else {
                    remainingPartETags.add(new PartETag(i, thisPartETagNumber));
                }
            }
        } catch (NullPointerException ignored) {
        }
        return remainingPartETags;
    }

    public static void removeAllPartETagsFromProp(String filePath) throws IOException {
        try {
            for (int i = 1; ; i++) {
                if (!GetProperties.getValueByKey(filePath, "partETag" + Integer.toString(i)).equals("")) {
                    GetProperties.writeProperties(filePath, "partETag" + Integer.toString(i), "");
                } else {
                    break;
                }
            }
        } catch (NullPointerException ignored) {

        }
    }

}
