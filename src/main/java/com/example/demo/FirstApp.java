package com.example.demo;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirstApp extends Application {

    private List<Image> selectedImages = new ArrayList<>();
    private Pane canvas;

    @Override
    public void start(Stage primaryStage) {
        // Create the root pane
        BorderPane root = new BorderPane();

        // Create the canvas pane
        canvas = new Pane();
        canvas.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, null, null)));
        canvas.setOnMouseClicked(event -> handleCanvasClick(event.getX(), event.getY()));

        // Create the save button
        Button saveButton = new Button("Сохранить");
        saveButton.setOnAction(event -> saveCompositeImage());

        try {
            ToggleButton button1 = createToggleButton("https://sun2-17.userapi.com/impg/PIYXJDU2mnfUdg73Mwbbol4WziPwp4uf6t4zDQ/KA54iaPBg2o.jpg?size=120x120&quality=96&sign=c9afb17c0e296eb11362991c99bc7679&type=album");
            ToggleButton button2 = createToggleButton("https://sun2-17.userapi.com/impg/vdNVVJj8k65xjmocfLJ2unUYVrMffZf1GTSDrg/sWGmetLMCcc.jpg?size=120x126&quality=96&sign=5010ac23f17d1aceee2de829d8f16ca5&type=album");
            ToggleButton button3 = createToggleButton("https://sun2-20.userapi.com/impg/nkdP25sUbazrq_JNR_bNWqUZor3BATHst9wzEA/_s7Ckq93uBY.jpg?size=120x120&quality=96&sign=97e2eb6855afa5db4ea1999d2af4c83c&type=album");

            VBox buttonGroup = new VBox(10, button1, button2, button3);
            buttonGroup.setPrefWidth(100);

            root.setLeft(buttonGroup);
            root.setCenter(canvas);
            root.setBottom(saveButton);
        }catch (Exception e){
            e.printStackTrace();
        }

        Scene scene = new Scene(root, 600, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Image Composition App");
        primaryStage.show();
    }

    private ToggleButton createToggleButton(String imagePath) {
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        ToggleButton button = new ToggleButton();
        button.setGraphic(imageView);
        button.setOnAction(event -> handleToggleButtonAction(button, image));

        return button;
    }

    private void handleToggleButtonAction(ToggleButton button, Image image) {
        if (button.isSelected()) {
            canvas.setOnMouseClicked(event -> handleCanvasClick(event.getX(), event.getY(), image));
        } else {
            canvas.setOnMouseClicked(event -> handleCanvasClick(event.getX(), event.getY()));
        }
    }

    private void handleCanvasClick(double x, double y, Image image) {
        if (image != null) {
            ImageView imageView = new ImageView(image);
            imageView.setLayoutX(x - image.getWidth() / 2);
            imageView.setLayoutY(y - image.getHeight() / 2);
            canvas.getChildren().add(imageView);
            selectedImages.add(image);
        }
    }

    private void handleCanvasClick(double x, double y) {
        if (selectedImages.size() > 0 && x >= 0 && y >= 0 && x <= canvas.getWidth() && y <= canvas.getHeight()) {
            ImageView imageView = new ImageView(selectedImages.get(selectedImages.size() - 1));
            imageView.setLayoutX(x - imageView.getImage().getWidth() / 2);
            imageView.setLayoutY(y - imageView.getImage().getHeight() / 2);
            canvas.getChildren().add(imageView);
        }
    }

    private void saveCompositeImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Images", "*.png"));
        File file = fileChooser.showSaveDialog(canvas.getScene().getWindow());

        if (file != null) {
            try {
                int width = (int) Math.round(canvas.getWidth());
                int height = (int) Math.round(canvas.getHeight());
                javafx.scene.SnapshotParameters params = new javafx.scene.SnapshotParameters();
                params.setFill(javafx.scene.paint.Color.TRANSPARENT);
                javafx.scene.image.WritableImage image = new javafx.scene.image.WritableImage(width, height);
                canvas.snapshot(params, image);

                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ImageIO.write(bufferedImage, "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}