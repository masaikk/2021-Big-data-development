package xyz.masaikk.win;

import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

public class Win1Test {

    @Test
    public void main() {
        System.out.println(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath());
    }
}