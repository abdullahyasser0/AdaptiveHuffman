package test;

import src.Encoder;
import src.Decoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class AdaptiveHiffmanTest {
    public Encoder encoder;
    public Decoder decoder;

    @BeforeEach
    public void setUp() {
         encoder = new Encoder();
         decoder = new Decoder();
    }

    @Test
    public void testEncode() {
        assertEquals("010000010010000100001000011101000101110", encoder.encode("ABCCCAAAA"));
    }

    @Test
    public void testDecode() {
        assertEquals("ABCCCAAAA", decoder.Decode("010000010010000100001000011101000101110"));
    }
}
