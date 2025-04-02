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

    public String binaryToSymbol(String BinaryAssi){
        int asciiNumber = Integer.parseInt(BinaryAssi, 2); // get the asci

        return Character.toString((char) asciiNumber);
    }

    public String Decode(String EncodedCode){
        StringBuilder plaintext = new StringBuilder();

        int SCL = 8;

        String firstSymbolCode = EncodedCode.substring(0, SCL); // extract first binary of asci
        String firstSymbol = binaryToSymbol(firstSymbolCode);

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
                        String substring = EncodedCode.substring(i+1, SCL+i+1); //get binary of ASCI
                        i+=SCL;
                        String substringvalue = binaryToSymbol(substring);
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
                        String substringvalue = binaryToSymbol(substring);
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

        Decoder decoder = new Decoder();
        String encodedOutput = decoder.Decode("010000010010000100001000011101000101110");
        System.out.println("Decmpresesd Text: " + encodedOutput);
    }

}



