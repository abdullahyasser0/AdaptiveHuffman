package visualization;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import src.HuffmanTree;
import src.Node;
import src.HuffmanTree.SwapListener;

public class HuffmanTreeVisualizer extends Application {
    private static HuffmanTree tree;
    private static Pane pane;
    private static volatile boolean isSwapInProgress = false;
    private static volatile boolean isPaused = false;
    private static Node preSwapRoot;

    @Override
    public void start(Stage primaryStage) {
        pane = new Pane();
        Button pauseButton = new Button("Pause");
        pauseButton.setLayoutX(10);
        pauseButton.setLayoutY(10);
        pauseButton.setOnAction(e -> {
            isPaused = !isPaused;
            pauseButton.setText(isPaused ? "Resume" : "Pause");
        });

        VBox root = new VBox(10);
        root.getChildren().addAll(pauseButton, pane);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Huffman Tree Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void triggerRedraw() {
        Platform.runLater(() -> {
            pane.getChildren().clear();
            if (tree != null && tree.getRoot() != null) {
                drawTree(tree.getRoot(), 400, 50, 200);
            }
        });
    }

    public static void insertSymbol(String symbol) {
        while (isSwapInProgress) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        while (isPaused) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        preSwapRoot = deepCopyNode(tree.getRoot());
        tree.insertSymbol(symbol);
        triggerRedraw();
    }

    public static void drawTree(Node node, double x, double y, double hSpacing) {
        if (node != null) {
            Rectangle rectangle = new Rectangle(x - 30, y - 20, 85, 40);
            rectangle.setFill(Color.LIGHTBLUE);
            rectangle.setStroke(Color.BLACK);
            rectangle.setUserData(node);

            String label = "N:" + node.getNumber() + " F:" + node.getFreq();
            if (node.getSymbol() != null) {
                label += " S:" + node.getSymbol();
            }

            Text text = new Text(x - 25, y, label);
            text.setUserData(node);

            pane.getChildren().addAll(rectangle, text);

            if (node.getLeft() != null) {
                double childX = x - hSpacing;
                double childY = y + 60;
                Line line = new Line(x, y + 20, childX, childY - 20);
                pane.getChildren().add(line);
                drawTree(node.getLeft(), childX, childY, hSpacing / 2);
            }

            if (node.getRight() != null) {
                double childX = x + hSpacing;
                double childY = y + 60;
                Line line = new Line(x, y + 20, childX, childY - 20);
                pane.getChildren().add(line);
                drawTree(node.getRight(), childX, childY, hSpacing / 2);
            }
        }
    }

    public static void highlightAndAnimateSwap(Node node1, Node node2, String reason) {
        isSwapInProgress = true;
        new Thread(() -> {
            try {
                // Step 1: Show pre-swap state with red highlights and reason for 5 seconds
                Platform.runLater(() -> {
                    pane.getChildren().clear();
                    drawTree(preSwapRoot, 400, 50, 200);

                    Rectangle rect1 = null, rect2 = null;
                    Text text1 = null, text2 = null;

                    for (var child : pane.getChildren()) {
                        if (child instanceof Rectangle) {
                            Node n = (Node) child.getUserData();
                            if (n.getNumber() == node1.getNumber()) rect1 = (Rectangle) child;
                            if (n.getNumber() == node2.getNumber()) rect2 = (Rectangle) child;
                        }
                        if (child instanceof Text) {
                            Node n = (Node) child.getUserData();
                            if (n.getNumber() == node1.getNumber()) text1 = (Text) child;
                            if (n.getNumber() == node2.getNumber()) text2 = (Text) child;
                        }
                    }

                    if (rect1 != null && rect2 != null) {
                        rect1.setFill(Color.RED);
                        rect2.setFill(Color.RED);
                        rect1.setStroke(Color.DARKRED);
                        rect2.setStroke(Color.DARKRED);
                    }

                    // Add reason text
                    Text reasonText = new Text(10, 550, "Swap Reason: " + reason); // Use the reason parameter
                    reasonText.setFill(Color.BLACK);
                    reasonText.setStyle("-fx-font-size: 16px;");
                    pane.getChildren().add(reasonText);
                });

                Thread.sleep(5000); // Hold pre-swap state with reason for 5 seconds

                // Step 2: Remove reason text and prepare for swap animation
                Platform.runLater(() -> {
                    // Remove the reason text by redrawing without it
                    pane.getChildren().clear();
                    drawTree(preSwapRoot, 400, 50, 200);

                    Rectangle rect1 = null, rect2 = null;
                    Text text1 = null, text2 = null;

                    for (var child : pane.getChildren()) {
                        if (child instanceof Rectangle) {
                            Node n = (Node) child.getUserData();
                            if (n.getNumber() == node1.getNumber()) rect1 = (Rectangle) child;
                            if (n.getNumber() == node2.getNumber()) rect2 = (Rectangle) child;
                        }
                        if (child instanceof Text) {
                            Node n = (Node) child.getUserData();
                            if (n.getNumber() == node1.getNumber()) text1 = (Text) child;
                            if (n.getNumber() == node2.getNumber()) text2 = (Text) child;
                        }
                    }

                    if (rect1 != null && rect2 != null) {
                        rect1.setFill(Color.RED);
                        rect2.setFill(Color.RED);
                        rect1.setStroke(Color.DARKRED);
                        rect2.setStroke(Color.DARKRED);
                    }

                    // Perform the swap animation
                    if (rect1 != null && rect2 != null && text1 != null && text2 != null) {
                        double dx1 = rect2.getX() - rect1.getX();
                        double dy1 = rect2.getY() - rect1.getY();
                        double dx2 = rect1.getX() - rect2.getX();
                        double dy2 = rect1.getY() - rect2.getY();

                        TranslateTransition tt1 = new TranslateTransition(Duration.millis(1000), rect1);
                        tt1.setByX(dx1);
                        tt1.setByY(dy1);

                        TranslateTransition tt2 = new TranslateTransition(Duration.millis(1000), rect2);
                        tt2.setByX(dx2);
                        tt2.setByY(dy2);

                        TranslateTransition tt3 = new TranslateTransition(Duration.millis(1000), text1);
                        tt3.setByX(dx1);
                        tt3.setByY(dy1);

                        TranslateTransition tt4 = new TranslateTransition(Duration.millis(1000), text2);
                        tt4.setByX(dx2);
                        tt4.setByY(dy2);

                        tt1.play();
                        tt2.play();
                        tt3.play();
                        tt4.play();
                    }
                });

                Thread.sleep(3000); // Wait for animation to complete

                // Step 3: Show post-swap state with red highlights for 5 seconds (no reason text)
                Platform.runLater(() -> {
                    pane.getChildren().clear();
                    drawTree(tree.getRoot(), 400, 50, 200);

                    Rectangle rect1 = null, rect2 = null;
                    for (var child : pane.getChildren()) {
                        if (child instanceof Rectangle) {
                            Node n = (Node) child.getUserData();
                            if (n.getNumber() == node1.getNumber()) rect1 = (Rectangle) child;
                            if (n.getNumber() == node2.getNumber()) rect2 = (Rectangle) child;
                        }
                    }

                    if (rect1 != null && rect2 != null) {
                        rect1.setFill(Color.RED);
                        rect2.setFill(Color.RED);
                        rect1.setStroke(Color.DARKRED);
                        rect2.setStroke(Color.DARKRED);
                    }
                });

                Thread.sleep(5000); // Hold post-swap state for 5 seconds

                // Step 4: Return to blue and final redraw
                Platform.runLater(() -> {
                    pane.getChildren().clear();
                    drawTree(tree.getRoot(), 400, 50, 200); // This redraws without the reason text
                });

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                isSwapInProgress = false;
            }
        }).start();
    }

    private static Node deepCopyNode(Node node) {
        if (node == null) return null;

        Node copy = new Node(node.getFreq(), node.getNumber(), node.getSymbol());
        copy.setLeft(deepCopyNode(node.getLeft()));
        copy.setRight(deepCopyNode(node.getRight()));

        if (copy.getLeft() != null) copy.getLeft().setParent(copy);
        if (copy.getRight() != null) copy.getRight().setParent(copy);

        return copy;
    }

    public static void main(String[] args) {
        tree = new HuffmanTree();
        tree.setVisualizer(new HuffmanTreeVisualizer());
        tree.setSwapListener(HuffmanTreeVisualizer::highlightAndAnimateSwap);

        new Thread(() -> {
            String str = "ABCCCAAAA";
            for (char symbol : str.toCharArray()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                insertSymbol(String.valueOf(symbol));
            }
        }).start();

        launch(args);
    }
}