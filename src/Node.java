// 1. Node Representation (Node.java)
// ● Represents individual nodes in the Huffman tree.
// ● Contains:
//      ○ Character data (for leaf nodes)
//      ○ Frequency count
//      ○ Left & right child references
//      ○ Parent reference
//      ○ Node number & order




package src;
public class Node {
    int freq;
    int NodeNumber;
    Node left,right,parent;
    String symbol;

    public Node(int freq,int NodeNumber,String symbol){
        this.freq = freq;
        this.NodeNumber = NodeNumber;
        this.symbol = symbol;
        this.parent=this.right=this.left= null;
    }
    public Node (){
        this.NodeNumber=0;
        this.freq=0;
        this.parent=this.right=this.left= null;
        this.symbol= null;
    }

    public void setParent(Node par){
        this.parent=par;
    }
    public void setLeft(Node left){
        this.left= left;
    }
    public void setRight(Node right) {
        this.right = right;
    }

    public int getNumber() {
        return this.NodeNumber;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public Node getParent() {
        return this.parent;
    }

    public Node getLeft() {
        return this.left;
    }
    public Node getRight() {
        return this.right;
    }
    public int getFreq() {
        return this.freq;
    }

    public void incrementFreq() {
        this.freq++;
    }
    public void setNNumber(int NNumber) {
        this.NodeNumber=NNumber;
    }
    public void setSymbol(String symbol) {
        this.symbol=symbol;
    }
}
