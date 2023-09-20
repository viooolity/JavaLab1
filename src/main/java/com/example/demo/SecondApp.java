package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.util.Random;

public class SecondApp extends Application {

    private final int windowWidth = 800;
    private final int windowHeight = 600;

    private Shape primitive;
    private Rectangle border;

    private double primitiveX;
    private double primitiveY;
    private double borderMargin = 10;

    private Random random = new Random();

    @Override
    public void start(Stage primaryStage) {
        // Create the root pane
        Pane root = new Pane();
        root.setPrefSize(windowWidth, windowHeight);

        // Generate a random primitive
        generateRandomPrimitive();

        // Create the border rectangle
        border = new Rectangle(
                primitiveX - borderMargin,
                primitiveY - borderMargin,
                primitive.getBoundsInLocal().getWidth() + borderMargin * 2,
                primitive.getBoundsInLocal().getHeight() + borderMargin * 2
        );
        border.setFill(null);
        border.setStroke(Color.BLACK);
        border.getStrokeDashArray().addAll(5d, 5d);
        border.setOpacity(0.5);

        // Add the primitive and border to the root pane
        root.getChildren().addAll(primitive, border);

        // Create the scene
        Scene scene = new Scene(root);

        // Handle key presses
        scene.setOnKeyPressed(event -> handleKeyPress(event.getCode()));

        // Set the scene on the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Graphics Primitive App");
        primaryStage.show();
    }

    private void generateRandomPrimitive() {
        double x = random.nextDouble() * (windowWidth - 100);
        double y = random.nextDouble() * (windowHeight - 100);
        double width = random.nextDouble() * 100 + 50;
        double height = random.nextDouble() * 100 + 50;

        int primitiveType = random.nextInt(4);

        switch (primitiveType) {
            case 0:
                primitive = new Line(x, y, x + width, y + height);
                break;
            case 1:
                primitive = new Circle(x + width / 2, y + height / 2, Math.min(width, height) / 2);
                break;
            case 2:
                primitive = new Ellipse(x + width / 2, y + height / 2, width / 2, height / 2);
                break;
            case 3:
                primitive = new Rectangle(x, y, width, height);
                break;
        }

        primitiveX = x;
        primitiveY = y;
    }

    private void handleKeyPress(KeyCode keyCode) {
        switch (keyCode) {
            case UP:
                primitiveY = Math.max(primitiveY - 10, 0);
                break;
            case DOWN:
                primitiveY = Math.min(primitiveY + 10, windowHeight - primitive.getBoundsInLocal().getHeight());
                break;
            case LEFT:
                primitiveX = Math.max(primitiveX - 10, 0);
                break;
            case RIGHT:
                primitiveX = Math.min(primitiveX + 10, windowWidth - primitive.getBoundsInLocal().getWidth());
                break;
            case PLUS:
            case ADD:
                if (primitive instanceof Circle) {
                    ((Circle) primitive).setRadius(((Circle) primitive).getRadius() + 10);
                } else if (primitive instanceof Ellipse) {
                    ((Ellipse) primitive).setRadiusY(((Ellipse) primitive).getRadiusY() + 10);
                } else if (primitive instanceof Rectangle) {
                    ((Rectangle) primitive).setHeight(((Rectangle) primitive).getHeight() + 10);
                }
                break;
            case MINUS:
            case SUBTRACT:
                if (primitive instanceof Circle) {
                    ((Circle) primitive).setRadius(Math.max(10, ((Circle) primitive).getRadius() - 10));
                } else if (primitive instanceof Ellipse) {
                    ((Ellipse) primitive).setRadiusY(Math.max(10, ((Ellipse) primitive).getRadiusY() - 10));
                } else if (primitive instanceof Rectangle) {
                    ((Rectangle) primitive).setHeight(Math.max(10, ((Rectangle) primitive).getHeight() - 10));
                }
                break;
            case LESS:
            case NUMPAD4:
                if (primitive instanceof Ellipse) {
                    ((Ellipse) primitive).setRadiusX(Math.max(10, ((Ellipse) primitive).getRadiusX() - 10));
                } else if (primitive instanceof Rectangle) {
                    ((Rectangle) primitive).setWidth(Math.max(10, ((Rectangle) primitive).getWidth() - 10));
                }
                break;
            case GREATER:
            case NUMPAD6:
                if (primitive instanceof Ellipse) {
                    ((Ellipse) primitive).setRadiusX(((Ellipse) primitive).getRadiusX() + 10);
                } else if (primitive instanceof Rectangle) {
                    ((Rectangle) primitive).setWidth(((Rectangle) primitive).getWidth() + 10);
                }
                break;
        }

        primitive.relocate(primitiveX, primitiveY);
        border.setX(primitiveX - borderMargin);
        border.setY(primitiveY - borderMargin);
        border.setWidth(primitive.getBoundsInLocal().getWidth() + borderMargin * 2);
        border.setHeight(primitive.getBoundsInLocal().getHeight() + borderMargin * 2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}