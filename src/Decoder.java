// 4. Decoding Process (Decoder.java)
// ● Steps:
//      ○ Traverse the Huffman tree using the encoded binary stream.
//      ○ Reconstruct and output the original text as the binary stream is processed.

/*
how it work ?
    take the short code and the encoded code and return the original text.
    lets say short code len = SCL;
    then the first SCL bits are taken from the encoded code and inserted to the tree

    then
    while the code dossent end
    take each symbol
    and traverse the tree
    if the symbol = 0 >> go left
    else >> go right
    if node.symbol != null && dosent = nyt
    extracr node symbol and add it to the tree
    if node.symbol != null && == nyt:
    skip take the next SCL from the table and insterten

    and so on




*/

package src;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Decoder {
    private HuffmanTree tree;

    public Decoder() {
        this.tree = new HuffmanTree();
    }

    public String Decode(String EncodedCode, HashMap<String, String> map){
        StringBuilder plaintext = new StringBuilder();
        Map.Entry<String, String> firstEntry = map.entrySet().iterator().next();
        int SCL = firstEntry.getKey().length(); //SCL >> short code lenght

        String firstSymbolCode = EncodedCode.substring(0, SCL);
        String firstSymbol = map.get(firstSymbolCode);
        plaintext.append(firstSymbol);
        tree.insertSymbol(firstSymbol);
        Node travers = tree.getRoot();
        StringBuilder path = new StringBuilder();
        for (int i = SCL; i<EncodedCode.length();i++) {
            if (EncodedCode.charAt(i) == '0') {
                path.append("0");
                travers = travers.getLeft();
                if (travers.getSymbol() != null) {
                    if (!Objects.equals(travers.getSymbol(), "NYT")) {
                        tree.insertSymbol(travers.getSymbol());
                        plaintext.append(travers.getSymbol());
                        travers = tree.getRoot();
                        path= new StringBuilder();
                    } else {
                        String substring = EncodedCode.substring(i+1, SCL+i+1);
                        i+=SCL;
                        String substringvalue = map.get(substring);
                        tree.insertSymbol(substringvalue);
                        plaintext.append(substringvalue);
                        travers = tree.getRoot();
                        path= new StringBuilder();
                    }

                }
            } else {
                travers = travers.getRight();
                path.append("1");
                if (travers.getSymbol() != null) {
                    if (!Objects.equals(travers.getSymbol(), "NYT")) {
                        tree.insertSymbol(travers.getSymbol());
                        plaintext.append(travers.getSymbol());
                        travers = tree.getRoot();
                        path= new StringBuilder();
                    } else {
                        String substring = EncodedCode.substring(i+1, SCL+i+1);
                        i+=SCL;
                        String substringvalue = map.get(substring);
                        tree.insertSymbol(substringvalue);
                        plaintext.append(substringvalue);
                        travers = tree.getRoot();
                        path= new StringBuilder();
                    }

                }
            }
        }
    return plaintext.toString();
    }

    public static void main(String[] args) {
        HashMap<String, String> huffmanMap = new HashMap<>();
        // key >> value
        // why short code is the key ? im extracting the shortCode and i want its value, so short code is the key
        huffmanMap.put("00", "A");
        huffmanMap.put("01", "B");

        huffmanMap.put("10", "C");

        Decoder decoder = new Decoder();
        String encodedOutput = decoder.Decode("000010010101000101110", huffmanMap);
        System.out.println("Decmpresesd Text: " + encodedOutput);
    }

}



