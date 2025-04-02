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
        HashMap<String, String> huffmanMap = new HashMap<>();
        huffmanMap.put("A", "00");
        huffmanMap.put("B", "01");
        huffmanMap.put("C", "10");
        assertEquals("000010010101000101110", encoder.encode("ABCCCAAAA", huffmanMap));
    }

    @Test
    public void testDecode() {
        HashMap<String, String> huffmanMap = new HashMap<>();
        huffmanMap.put("00", "A");
        huffmanMap.put("01", "B");
        huffmanMap.put("10", "C");
        assertEquals("ABCCCAAAA", decoder.Decode("000010010101000101110", huffmanMap));
    }
}
