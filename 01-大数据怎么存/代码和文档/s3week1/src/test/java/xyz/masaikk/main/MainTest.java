package xyz.masaikk.main;

import org.junit.Test;
import xyz.masaikk.win.Win2;

import java.io.IOException;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void initialize() throws IOException, InterruptedException {
        Win2.main(null);
    }
}