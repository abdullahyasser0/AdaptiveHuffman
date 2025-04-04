//some comments


package src;

import visualization.HuffmanTreeVisualizer;

import java.util.Objects;

public class HuffmanTree {
    Node root;
    private Node nyt; // Keep track of the NYT node
    private SwapListener swapListener;
    private HuffmanTreeVisualizer visualizer;

    public HuffmanTree() {
        root = new Node(0, 100, null);
    }

    public void setSwapListener(SwapListener listener) { // Removed unused String parameter
        this.swapListener = listener;
    }

    public void setVisualizer(HuffmanTreeVisualizer visualizer) {
        this.visualizer = visualizer;
    }

    public void triggerRedraw() {
        if (visualizer != null) {
            visualizer.triggerRedraw();  // Calls the visualizer's redraw method
        }
    }

    public boolean isEmpty() {
        return root.getRight() == null && root.getLeft() == null;
    }

    public interface SwapListener {
        void onSwap(Node node1, Node node2, String reason); // Updated to include reason
    }

    public void insertSymbol(String symbol) {
        if (getNode(root, symbol) != null) {
            incrementSymbolFreq(symbol);
        } else {
            addNewNode(symbol);
        }
    }

    public Node getRoot() {
        return root;
    }

    public String getSymbolCode(String symbol) {
        return getSymbolCodeHelper(root, symbol, "");
    }

    private String getSymbolCodeHelper(Node node, String symbol, String path) {
        if (node == null) {
            return null;
        }
        if (Objects.equals(node.getSymbol(), symbol)) {
            return path;
        }
        String leftResult = getSymbolCodeHelper(node.getLeft(), symbol, path + "0");
        if (leftResult != null) {
            return leftResult;
        }
        if (node.getLeft() == null && node.getRight() == null) {
            return null;
        }

        return getSymbolCodeHelper(node.getRight(), symbol, path + "1");
    }

    public void addNewNode(String symbol) { // new symbol is inserted to tree
        Node parent;
        if (getNode(root, "NYT") == null) {
            parent = root;
        } else {
            parent = nyt;
        }

        int parentNumber = parent.getNumber();
        int leftNumber = parentNumber - 2;
        int rightNumber = parentNumber - 1;

        Node left = new Node(0, leftNumber, "NYT");
        Node right = new Node(1, rightNumber, symbol);
        left.setParent(parent);
        right.setParent(parent);
        parent.setLeft(left);
        parent.setRight(right);
        parent.incrementFreq();
        parent.setSymbol(null);
        nyt = left; // Update NYT reference
        triggerRedraw();

        while (parent != root) {
            parent = parent.getParent();
            Node swapNode = getSwappable(root, parent);

            if (swapNode != null) {
                System.out.println(symbol);
                swap(swapNode, parent);
            }
            parent.incrementFreq();
        }
    }

    public void incrementSymbolFreq(String symbol) {
        Node node = getNode(root, symbol);
        triggerRedraw();
        while (node != root) {
            Node swapNode = getSwappable(root, node);
            if (swapNode != null) {
                swap(swapNode, node);
            }
            node.incrementFreq();
            node = node.getParent();
        }
        node.incrementFreq();
    }

    public Node getNode(Node root, String searchSymbol) {
        if (root == null) return null;
        if (Objects.equals(root.getSymbol(), searchSymbol)) return root;

        Node leftResult = getNode(root.getLeft(), searchSymbol);
        if (leftResult != null) return leftResult;

        return getNode(root.getRight(), searchSymbol);
    }

    public Node getSwappable(Node root, Node myNode) {
        if (root == null) return null;
        if (swappable(myNode, root)) return root;

        Node leftResult = getSwappable(root.getLeft(), myNode);
        if (leftResult != null) return leftResult;

        return getSwappable(root.getRight(), myNode);
    }

    public boolean swappable(Node myNode, Node node) {
        return myNode.getNumber() < node.getNumber() && myNode.getFreq() >= node.getFreq() && myNode.getParent() != node && myNode != node;
    }

    public void swap(Node myNode, Node node) {
        // Generate the reason based on swappable conditions
        String reason = "Node " + myNode.getNumber() + " (Freq: " + myNode.getFreq() +
                ") has higher or equal frequency than Node " + node.getNumber() +
                " (Freq: " + node.getFreq() + ") and lower number";

        // Notify the listener before the swap occurs with the reason
        if (swapListener != null) {
            swapListener.onSwap(myNode, node, reason);
        }

        // Perform the swap
        Node myParent = myNode.getParent();
        Node nodeParent = node.getParent();

        if (myParent.getLeft() == myNode) myParent.setLeft(node);
        else myParent.setRight(node);

        if (nodeParent.getLeft() == node) nodeParent.setLeft(myNode);
        else nodeParent.setRight(myNode);

        myNode.setParent(nodeParent);
        node.setParent(myParent);

        int temp = myNode.getNumber();
        myNode.setNNumber(node.getNumber());
        node.setNNumber(temp);
    }

    public void printTree(Node root) {
        if (root == null) return;
        System.out.print(root.getSymbol() + " ");
        printTree(root.getLeft());
        printTree(root.getRight());
    }

    public static void main(String[] args) {
        HuffmanTree tree = new HuffmanTree();
        String str = "ABCCCAAAA";
        for (char symbol : str.toCharArray()) {
            tree.insertSymbol(String.valueOf(symbol));
        }

        tree.printTree(tree.root);
        System.out.print(tree.getSymbolCode("B"));
    }
}