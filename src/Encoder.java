// 3. Encoding Process (Encoder.java)
// ● Steps:
//      ○ Retrieve binary codes for characters from the Huffman tree.
//      ○ Append the NYT code followed by the ASCII representation for new characters.
//      ○ Update the tree dynamically with frequency adjustments.

/*
what encode should do ?
    Take a plain text,shortCode and return the code

    -take the plainText.
        -where should it take the text from?
            -we will see work as its a variable
    for each symbol:
        -if its the first symbol, then type the symbol short code
        -else :
            if its found in the tree :
                -type symbol code
            else
                type NYT code and symbol short code;




*/
package src;
import java.util.HashMap;

public class Encoder {
    private HuffmanTree tree;

    // Constructor
    public Encoder() {
        this.tree = new HuffmanTree(); // Initialize the HuffmanTree
    }

    // Encoding Function
    public String encode(String plainText, HashMap<String, String> map) {
        StringBuilder encodedText = new StringBuilder();

        for (char symbol : plainText.toCharArray()) {
            String symbolStr = String.valueOf(symbol);

            if (tree.isEmpty()) { // First symbol case
                encodedText.append(map.getOrDefault(symbolStr, ""));
                tree.insertSymbol(symbolStr);
            } else {
                String code = tree.getSymbolCode(symbolStr);
                if (code != null) {
                    encodedText.append(code);
                    tree.insertSymbol(symbolStr);
                } else { // If symbol is new, encode as "NYT" + its Huffman code
                    String nytCode = tree.getSymbolCode("NYT");
                    if (nytCode != null) {
                        encodedText.append(nytCode);
                    }
                    encodedText.append(map.getOrDefault(symbolStr, ""));
                    tree.insertSymbol(symbolStr);
                }
            }
        }

        return encodedText.toString();
    }

    // Main method for testing
    public static void main(String[] args) {
        HashMap<String, String> huffmanMap = new HashMap<>();
        huffmanMap.put("A", "00");
        huffmanMap.put("B", "01");

        huffmanMap.put("C", "10");

        Encoder encoder = new Encoder();
        String encodedOutput = encoder.encode("ABCCCAAAA", huffmanMap);
        System.out.println("Encoded Text: " + encodedOutput);
    }
}
