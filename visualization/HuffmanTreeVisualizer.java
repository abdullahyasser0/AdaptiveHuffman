package visualization;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import src.HuffmanTree;
import src.Node;

public class HuffmanTreeVisualizer extends Application {
    private HuffmanTree tree;
    private Pane pane;

    @Override
    public void start(Stage primaryStage) {
        pane = new Pane();
        tree = new HuffmanTree();
        tree.setSwapListener(this::highlightSwap);  // Set the swap listener

        // Simulate insertion of symbols
        insertSymbol("A");
        new Thread(() -> {
            try {
                Thread.sleep(1000); insertSymbol("B");
                Thread.sleep(1000); insertSymbol("C");
                Thread.sleep(1000); insertSymbol("C");
                Thread.sleep(1000); insertSymbol("C");
                Thread.sleep(1000); insertSymbol("A"); // Increment frequency of 'A'
                Thread.sleep(1000); insertSymbol("A");
                Thread.sleep(1000); insertSymbol("A");
                Thread.sleep(1000); insertSymbol("A");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        Scene scene = new Scene(pane, 800, 600);
        primaryStage.setTitle("Huffman Tree Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void insertSymbol(String symbol) {
        tree.insertSymbol(symbol);
        Platform.runLater(() -> {
            pane.getChildren().clear();
            drawTree(pane, tree.getRoot(), 400, 50, 200, null, null);
        });
    }

    // Method to draw the tree with the option to highlight two nodes
    private void drawTree(Pane pane, Node node, double x, double y, double hSpacing, Node highlight1, Node highlight2) {
        if (node != null) {
            Rectangle rectangle = new Rectangle(x - 30, y - 20, 85, 40);
            // Set the color to red if the node is being swapped, otherwise light blue
            rectangle.setFill((node == highlight1 || node == highlight2) ? Color.RED : Color.LIGHTBLUE);
            rectangle.setStroke(Color.BLACK);

            String label = "N:" + node.getNumber() + " F:" + node.getFreq();
            if (node.getSymbol() != null) {
                label += " S:" + node.getSymbol();
            }

            Text text = new Text(x - 25, y, label);

            pane.getChildren().addAll(rectangle, text);

            if (node.getLeft() != null) {
                double childX = x - hSpacing;
                double childY = y + 60;
                Line line = new Line(x, y + 20, childX, childY - 20);
                pane.getChildren().add(line);
                drawTree(pane, node.getLeft(), childX, childY, hSpacing / 2, highlight1, highlight2);
            }

            if (node.getRight() != null) {
                double childX = x + hSpacing;
                double childY = y + 60;
                Line line = new Line(x, y + 20, childX, childY - 20);
                pane.getChildren().add(line);
                drawTree(pane, node.getRight(), childX, childY, hSpacing / 2, highlight1, highlight2);
            }
        }
    }

    // Highlight the swapped nodes by updating the tree visualization
    private void highlightSwap(Node node1, Node node2) {
        Platform.runLater(() -> {
            System.out.println("Swapping: " + node1.getNumber() + " and " + node2.getNumber()); // Print swapped nodes in terminal
            pane.getChildren().clear();  // Clear current visualization

            // Redraw the tree with swapped nodes highlighted
            drawTree(pane, tree.getRoot(), 400, 50, 200, node1, node2);

            try {
                Thread.sleep(1000); // Pause for 1 second to show the swap highlight
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Clear the highlighted nodes after pause
            pane.getChildren().clear();
            drawTree(pane, tree.getRoot(), 400, 50, 200, null, null);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
