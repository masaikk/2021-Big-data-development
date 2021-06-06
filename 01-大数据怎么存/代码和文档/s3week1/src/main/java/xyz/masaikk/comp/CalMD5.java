package xyz.masaikk.comp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CalMD5 {
    static public String calculate(Path path) {
        try (InputStream is = Files.newInputStream(Paths.get(String.valueOf(path)))) {
            //calculate md5 and return it.
            return org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
