package xyz.masaikk.comp;

import com.amazonaws.services.s3.model.PartETag;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class GetPropertiesTest {

    @Test
    public void main() throws IOException {
        GetProperties.writeProperties("config.properties","partETag1","40c52ae3afd22c1cea2cfed1a1276f44");
    }


    @Test
    public void getValueByKey() {
        GetProperties.getValueByKey("config.properties","filePath");
    }

    @Test
    public void getEtags(){
        ArrayList<PartETag> partETags=GetProperties.getPartETagsByProp("config.properties");
        System.out.println(partETags.size());
    }

    @Test
    public void deleteETags() throws IOException {
        GetProperties.removeAllPartETagsFromProp("config.properties");
    }
}