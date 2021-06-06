package xyz.masaikk.comp;

import org.junit.Test;

import java.io.File;
import java.util.Objects;

import static org.junit.Assert.*;

public class GetFilesListTest {

    @Test
    public void getFileList() {
//        System.out.println(GetFilesList.getFileList("D:\\librosa-0.6.0"));
        File file = new File(Objects.requireNonNull(GetProperties.getValueByKey("config.properties", "filePath")));
        File[] files=file.listFiles();
        for (File f:files
             ) {
            System.out.println(f);
        }
    }
}