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
    public String encode(String plainText) {
        StringBuilder encodedText = new StringBuilder();  // this should contain binary representation

        for (char symbol : plainText.toCharArray()) {
            String symbolStr = String.valueOf(symbol);
            int asciiValue = (int) symbol;
            String binary = String.format("%8s", Integer.toBinaryString(asciiValue)).replace(' ', '0'); //extract acsi binary code
            if (tree.isEmpty()) { // First symbol case
                encodedText.append(binary);
                tree.insertSymbol(symbolStr);
            } else { //if the tree already contain
                String code = tree.getSymbolCode(symbolStr);
                if (code != null) {
                    encodedText.append(code);
                    tree.insertSymbol(symbolStr);
                } else { // If symbol is new, encode as "NYT" + its ASCI code
                    String nytCode = tree.getSymbolCode("NYT");
                    if (nytCode != null) {
                        encodedText.append(nytCode); // append the nyt
                    }
                    encodedText.append(binary); //append its acsi binary vcode
                    tree.insertSymbol(symbolStr);
                }
            }
        }

        return encodedText.toString();
    }

    // Main method for testing
    public static void main(String[] args) {

        Encoder encoder = new Encoder();
        String encodedOutput = encoder.encode("ABCCCAAAA");
        System.out.println("Encoded Text: " + encodedOutput);
    }
}
