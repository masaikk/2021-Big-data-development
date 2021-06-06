package xyz.masaikk.comp;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoadConfigTest {

    @Test
    public void updatePro() {
        final String filePath="xyz/masaikk/config.properties";
        LoadConfig.updatePro(filePath,"filePath","..");
    }
}