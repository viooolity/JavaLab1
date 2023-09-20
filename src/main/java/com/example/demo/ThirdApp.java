package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ThirdApp extends Application {

    private Canvas canvas;
    private GraphicsContext gc;
    private ToggleButton selectedPrimitive;
    private Color fillColor;
    private double lineWidth;
    private String borderType;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Графический редактор");

        // Создание палитры с кнопками примитивов
        HBox palette = createPalette();

        // Создание панели управления
        HBox controlPanel = createControlPanel();

        // Создание области отображения рисунка
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();

        // Обработка событий клика мыши на области отображения рисунка
        canvas.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double x = event.getX();
                double y = event.getY();

                drawPrimitive(x, y);
            }
        });

        // Создание основной компоновки
        BorderPane root = new BorderPane();
        root.setTop(palette);
        root.setLeft(controlPanel);
        root.setCenter(canvas);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createPalette() {
        ToggleButton rectangleButton = new ToggleButton("Прямоугольник");
        ToggleButton ellipseButton = new ToggleButton("Эллипс");
        ToggleButton lineButton = new ToggleButton("Линия");

        rectangleButton.setOnAction(event -> {
            selectedPrimitive = rectangleButton;
        });

        ellipseButton.setOnAction(event -> {
            selectedPrimitive = ellipseButton;
        });

        lineButton.setOnAction(event -> {
            selectedPrimitive = lineButton;
        });

        HBox palette = new HBox(rectangleButton, ellipseButton, lineButton);
        palette.getStyleClass().add("palette");

        return palette;
    }

    private HBox createControlPanel() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setOnAction(event -> {
            fillColor = colorPicker.getValue();
        });

        Slider lineWidthSlider = new Slider(1, 10, 1);
        lineWidthSlider.setShowTickMarks(true);
        lineWidthSlider.setMajorTickUnit(1);
        lineWidthSlider.setMinorTickCount(0);
        lineWidthSlider.setBlockIncrement(1);
        lineWidthSlider.setOnMouseClicked(event -> {
            lineWidth = lineWidthSlider.getValue();
        });

        ComboBox<String> borderTypeComboBox = new ComboBox<>();
        borderTypeComboBox.getItems().addAll("сплошная", "пунктирная", "точечная");
        borderTypeComboBox.setValue("сплошная");
        borderTypeComboBox.setOnAction(event -> {
            borderType = borderTypeComboBox.getValue();
        });

        HBox controlPanel = new HBox(colorPicker, lineWidthSlider, borderTypeComboBox);
        controlPanel.getStyleClass().add("control-panel");

        return controlPanel;
    }

    private void drawPrimitive(double x, double y) {
        if (selectedPrimitive == null) {
            return;
        }

        gc.setFill(fillColor);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(lineWidth);

        if (borderType.equals("сплошная")) {
            gc.setLineDashes();
        } else if (borderType.equals("пунктирная")) {
            gc.setLineDashes(5);
        } else if (borderType.equals("точечная")) {
            gc.setLineDashes(2, 2);
        }

        if (selectedPrimitive.getText().equals("Прямоугольник")) {
            gc.fillRect(x, y, 100, 80);
            gc.strokeRect(x, y, 100, 80);
        } else if (selectedPrimitive.getText().equals("Эллипс")) {
            gc.fillOval(x, y, 100, 80);
            gc.strokeOval(x, y, 100, 80);
        } else if (selectedPrimitive.getText().equals("Линия")) {
            gc.strokeLine(x, y, x + 100, y + 80);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}