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
            drawTree(pane, tree.getRoot(), 400, 50, 200);
        });
    }

    private void drawTree(Pane pane, Node node, double x, double y, double hSpacing) {
        if (node != null) {
            Rectangle rectangle = new Rectangle(x - 30, y - 20, 85, 40);
            rectangle.setFill(Color.LIGHTBLUE);
            rectangle.setStroke(Color.BLACK);

            String label = "N:" + node.NNumber() + " F:" + node.getFreq();
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
                drawTree(pane, node.getLeft(), childX, childY, hSpacing / 2);
            }

            if (node.getRight() != null) {
                double childX = x + hSpacing;
                double childY = y + 60;
                Line line = new Line(x, y + 20, childX, childY - 20);
                pane.getChildren().add(line);
                drawTree(pane, node.getRight(), childX, childY, hSpacing / 1);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}