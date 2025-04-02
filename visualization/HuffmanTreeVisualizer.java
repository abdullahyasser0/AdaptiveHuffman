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
import src.HuffmanTree;
import src.Node;

public class HuffmanTreeVisualizer extends Application {
    private HuffmanTree tree;
    private Pane pane;
    private volatile boolean isSwapInProgress = false;
    private volatile boolean isPaused = false;
    private Node preSwapRoot;  // Store the root before the swap

    @Override
    public void start(Stage primaryStage) {
        pane = new Pane();
        tree = new HuffmanTree();
        tree.setVisualizer(this);  // Set visualizer to the tree
        tree.setSwapListener(this::highlightSwap);

        // Create a pause button
        Button pauseButton = new Button("Pause");
        pauseButton.setLayoutX(10);
        pauseButton.setLayoutY(10);
        pauseButton.setOnAction(e -> {
            isPaused = !isPaused;
            pauseButton.setText(isPaused ? "Resume" : "Pause");
        });

        // Add the button to a VBox to manage layout
        VBox root = new VBox(10);
        root.getChildren().addAll(pauseButton, pane);

        // Simulate insertion of symbols
        new Thread(() -> {
            try {
                insertSymbol("A");
                Thread.sleep(2000);
                insertSymbol("B");
                Thread.sleep(2000);
                insertSymbol("C");
                Thread.sleep(2000);
                insertSymbol("C");
                Thread.sleep(2000);
                insertSymbol("C");
                Thread.sleep(2000);
                insertSymbol("A");
                Thread.sleep(2000);
                insertSymbol("A");
                Thread.sleep(2000);
                insertSymbol("A");
                Thread.sleep(2000);
                insertSymbol("A");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Huffman Tree Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Trigger a redraw of the tree, useful for external calls (e.g., from HuffmanTree class)
    public void triggerRedraw() {
        new Thread(() -> {
            try {
                // Delay before starting the redraw (optional)
                Thread.sleep(1000);  // Adjust the delay time as needed

                // Redraw the tree after the initial delay
                Platform.runLater(() -> {
                    // Ensure pane is cleared and re-drawn
                    if (pane != null) {
                        pane.getChildren().clear();  // Clear the existing UI elements
                        if (tree.getRoot() != null) {
                            System.out.println("Redrawing tree...");
                            drawTree(pane, tree.getRoot(), 400, 50, 200, null, null);  // Redraw the tree with the current root
                        } else {
                            System.out.println("Root node is null. Cannot redraw tree.");
                        }
                    } else {
                        System.out.println("Pane is null. Cannot clear and redraw tree.");
                    }
                });

                // Sleep for 1 second after drawing
                Thread.sleep(1000);  // Adjust the delay time as needed
                System.out.println("Pause after redraw.");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }



    private void insertSymbol(String symbol) {
        // Wait for any ongoing swap visualization to complete
        while (isSwapInProgress) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Check if paused
        while (isPaused) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Store the pre-swap state of the tree
        preSwapRoot = deepCopyNode(tree.getRoot());

        // Perform the insertion (which may trigger swaps)
        tree.insertSymbol(symbol);

        // Redraw the tree after the swap is complete
        Platform.runLater(() -> {
            pane.getChildren().clear();
            drawTree(pane, tree.getRoot(), 400, 50, 200, null, null);
        });
    }

    // Method to draw the tree with the option to highlight two nodes
    private void drawTree(Pane pane, Node node, double x, double y, double hSpacing, Node highlight1, Node highlight2) {
        if (node != null) {
            Rectangle rectangle = new Rectangle(x - 30, y - 20, 85, 40);

            // Check if node should be highlighted
            if (highlight1!=null) {
                if (node.getNumber() == highlight1.getNumber() || node.getNumber() == highlight2.getNumber()) {
                    rectangle.setFill(Color.RED);
                    rectangle.setStroke(Color.DARKRED);
                } else {
                    rectangle.setFill(Color.LIGHTBLUE);
                    rectangle.setStroke(Color.BLACK);
                }
            }else {
                rectangle.setFill(Color.LIGHTBLUE);
                rectangle.setStroke(Color.BLACK);
            }

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

    // Highlight the nodes before swapping
    private void highlightSwap(Node node1, Node node2) {
        isSwapInProgress = true;
        new Thread(() -> {
            try {
                // Step 1: Clear the pane first
                Platform.runLater(() -> {
                    pane.getChildren().clear();
                });

                // Step 2: Wait for 1 second
                Thread.sleep(0);

                // Step 3: Highlight the nodes in their pre-swap positions using the pre-swap root
                Platform.runLater(() -> {
                    System.out.println("Highlighting nodes before swap: " + node1.getNumber() + " <-> " + node2.getNumber());
                    drawTree(pane, preSwapRoot, 400, 50, 200, node1, node2);
                });

                // Step 4: Keep the highlighted nodes visible for 3 seconds
                long startTime = System.currentTimeMillis();
                long pauseTime = 3000;
                while (System.currentTimeMillis() - startTime < pauseTime) {
                    if (isPaused) {
                        Thread.sleep(100);
                        startTime = System.currentTimeMillis() - (pauseTime - 3000);
                    } else {
                        Thread.sleep(100);
                    }
                }

                // Step 5: Redraw the tree to reflect the swap (using the current root)
                Platform.runLater(() -> {
                    System.out.println("Redrawing tree after swap...");
                    pane.getChildren().clear();
                    drawTree(pane, tree.getRoot(), 400, 50, 200, null, null);
                });

                // Step 6: Keep the updated tree visible for 4 seconds
                startTime = System.currentTimeMillis();
                pauseTime = 4000;
                while (System.currentTimeMillis() - startTime < pauseTime) {
                    if (isPaused) {
                        Thread.sleep(100);
                        startTime = System.currentTimeMillis() - (pauseTime - 4000);
                    } else {
                        Thread.sleep(100);
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                isSwapInProgress = false;
            }
        }).start();
    }


    // Deep copy method for Node to preserve the pre-swap state
    private Node deepCopyNode(Node node) {
        if (node == null) {
            return null;
        }

        Node copy = new Node(node.getFreq(), node.getNumber(), node.getSymbol());
        copy.setLeft(deepCopyNode(node.getLeft()));
        copy.setRight(deepCopyNode(node.getRight()));

        // Set parent references for the copied nodes
        if (copy.getLeft() != null) {
            copy.getLeft().setParent(copy);
        }
        if (copy.getRight() != null) {
            copy.getRight().setParent(copy);
        }

        return copy;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
