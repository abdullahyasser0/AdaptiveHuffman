import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BinarySearchTree extends Application {
    private Node root;

    // Node class for the tree
    private class Node {
        int value;
        Node left, right;

        Node(int value) {
            this.value = value;
            left = right = null;
        }
    }

    // Insert method
    public void insert(int value) {
        root = insertRec(root, value);
    }

    private Node insertRec(Node root, int value) {
        if (root == null) {
            root = new Node(value);
            return root;
        }
        if (value < root.value)
            root.left = insertRec(root.left, value);
        else if (value > root.value)
            root.right = insertRec(root.right, value);
        return root;
    }

    @Override
    public void start(Stage primaryStage) {
        // Create a sample tree
        insert(50);
        insert(30);
        insert(70);
        insert(20);
        insert(40);
        insert(60);
        insert(80);

        Pane pane = new Pane();
        drawTree(pane, root, 400, 50, 200);

        Scene scene = new Scene(pane, 800, 600);
        primaryStage.setTitle("Binary Search Tree Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Draw the tree recursively
    private void drawTree(Pane pane, Node node, double x, double y, double hSpacing) {
        if (node != null) {
            // Draw node
            Circle circle = new Circle(x, y, 20);
            circle.setFill(Color.LIGHTBLUE);
            circle.setStroke(Color.BLACK);

            Text text = new Text(x - 5, y + 5, String.valueOf(node.value));

            pane.getChildren().addAll(circle, text);

            // Draw left child
            if (node.left != null) {
                double childX = x - hSpacing;
                double childY = y + 60;
                Line line = new Line(x, y + 20, childX, childY - 20);
                pane.getChildren().add(line);
                drawTree(pane, node.left, childX, childY, hSpacing / 2);
            }

            // Draw right child
            if (node.right != null) {
                double childX = x + hSpacing;
                double childY = y + 60;
                Line line = new Line(x, y + 20, childX, childY - 20);
                pane.getChildren().add(line);
                drawTree(pane, node.right, childX, childY, hSpacing / 2);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}