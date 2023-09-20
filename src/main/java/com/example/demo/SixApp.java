package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SixApp extends Application {
    private final Group rootGroup = new Group();
    private final Xform moleculeGroup = new Xform();
    private final PerspectiveCamera camera = new PerspectiveCamera(true);

    private final List<Atom> atoms = new ArrayList<>();

    private static final String FILE_PATH = "C:\\Users\\iserg\\Downloads\\XYZ.xyz";
//"C:\Users\iserg\Downloads\XYZ.xyz"
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        loadAtomsFromFile();
        initCamera();
        buildMolecule();

        Scene scene = new Scene(rootGroup, 800, 600, true);
        handleKeyboard(scene);

        rootGroup.getChildren().add(moleculeGroup);
        Button saveButton = new Button("Сохранить");
        saveButton.setOnAction(event -> saveSceneSnapshot(scene));

        VBox vbox = new VBox(saveButton);
        vbox.setSpacing(10);
        vbox.setTranslateX(10);
        vbox.setTranslateY(10);

        rootGroup.getChildren().add(vbox);


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void saveSceneSnapshot(Scene scene) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить снимок сцены");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpg"),
                new FileChooser.ExtensionFilter("GIF", "*.gif")
        );

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                WritableImage image = scene.snapshot(null);
                String extension = getFileExtension(file.getName());
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), extension, file);
                showSaveSuccessDialog();
            } catch (IOException e) {
                showSaveErrorDialog();
            }
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    private void showSaveSuccessDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успех");
        alert.setHeaderText(null);
        alert.setContentText("Снимок сцены успешно сохранен.");
        alert.showAndWait();
    }

    private void showSaveErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText("Не удалось сохранить снимок сцены.");
        alert.showAndWait();
    }

    private void initCamera() {
        rootGroup.getChildren().add(camera);

        camera.setNearClip(0.1);
        camera.setFarClip(100);
        camera.setTranslateZ(100.0);
    }

    private void buildMolecule() {
        for (int i = 0; i < atoms.size(); i++) {
            Atom atom1 = atoms.get(i);
            Sphere sphere1 = createAtomSphere(atom1);

            for (int j = i + 1; j < atoms.size(); j++) {
                Atom atom2 = atoms.get(j);
                Sphere sphere2 = createAtomSphere(atom2);

                Cylinder cylinder = createBondCylinder(atom1, atom2);

                moleculeGroup.getChildren().addAll(sphere2, cylinder);
            }

            moleculeGroup.getChildren().add(sphere1);
        }
    }

    private Sphere createAtomSphere(Atom atom) {
        Sphere sphere = new Sphere(atom.getRadius());
        sphere.setTranslateX(atom.getX());
        sphere.setTranslateY(atom.getY());
        sphere.setTranslateZ(atom.getZ());

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(atom.getColor());
        material.setSpecularColor(Color.WHITE);

        sphere.setMaterial(material);
        return sphere;
    }

    private Cylinder createBondCylinder(Atom atom1, Atom atom2) {
        double x1 = atom1.getX();
        double y1 = atom1.getY();
        double z1 = atom1.getZ();

        double x2 = atom2.getX();
        double y2 = atom2.getY();
        double z2 = atom2.getZ();

        Cylinder cylinder = new Cylinder();
        cylinder.setRadius(1);
        cylinder.setHeight(Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2) + Math.pow((z2 - z1), 2))); // Задайте высоту цилиндра

        cylinder.setTranslateX((x1 + x2) / 2);
        cylinder.setTranslateY((y1 + y2) / 2);
        cylinder.setTranslateZ((z1 + z2) / 2);

        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = new Point3D(x2 - x1, y2 - y1, z2 - z1);
        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = -Math.acos(diff.normalize().dotProduct(yAxis));
        cylinder.getTransforms().addAll(new Rotate(Math.toDegrees(angle), axisOfRotation));

        return cylinder;
    }

    private void loadAtomsFromFile() {
        try {
            File file = new File(SixApp.FILE_PATH);
            Scanner scanner = new Scanner(file);

            int atomCount = Integer.parseInt(scanner.nextLine()); // Читаем первую строку как число
            // Пропускаем вторую строку
            scanner.nextLine();

            for (int i = 0; i < atomCount; i++) {
                String line = scanner.nextLine();
                String[] atomData = line.split(" ");

                if (atomData.length >= 4) {
                    String element = atomData[0].substring(0, 1); // Берем первую букву
                    double x = Double.parseDouble(atomData[1]);
                    double y = Double.parseDouble(atomData[2]);
                    double z = Double.parseDouble(atomData[3]);

                    Atom atom = new Atom(element, x + 100, y + 60, z + 40);
                    atoms.add(atom);
                } else {
                    System.out.println("Некорректный формат строки: " + line);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void handleKeyboard(Scene scene) {
        final double[] scale = {1.0};
        final double[] rotationAngle = {0.0};

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W -> moleculeGroup.setTranslateY(moleculeGroup.getTranslateY() - 10);
                case S -> moleculeGroup.setTranslateY(moleculeGroup.getTranslateY() + 10);
                case A -> moleculeGroup.setTranslateX(moleculeGroup.getTranslateX() - 10);
                case D -> moleculeGroup.setTranslateX(moleculeGroup.getTranslateX() + 10);
                case Q -> {
                    scale[0] += 0.1;
                    moleculeGroup.setScaleX(scale[0]);
                    moleculeGroup.setScaleY(scale[0]);
                    moleculeGroup.setScaleZ(scale[0]);
                }
                case E -> {
                    scale[0] -= 0.1;
                    moleculeGroup.setScaleX(scale[0]);
                    moleculeGroup.setScaleY(scale[0]);
                    moleculeGroup.setScaleZ(scale[0]);
                }
                case SPACE -> {
                    moleculeGroup.setTranslateX(0);
                    moleculeGroup.setTranslateY(0);
                    moleculeGroup.setTranslateZ(-400);
                    moleculeGroup.setRotationAxis(Rotate.X_AXIS);
                    rotationAngle[0] = 0.0;
                    moleculeGroup.setRotate(rotationAngle[0]);
                    moleculeGroup.setScaleX(1.0);
                    moleculeGroup.setScaleY(1.0);
                    moleculeGroup.setScaleZ(1.0);
                }
            }
        });
    }

    // Вспомогательный класс для группировки трансформаций
    private static class Xform extends Group {
        private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        private final Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);

        private Xform() {
            super();
            getTransforms().addAll(rotateX, rotateY, rotateZ);
        }

        private void setRotateX(double angle) {
            rotateX.setAngle(angle);
        }

        private void setRotateY(double angle) {
            rotateY.setAngle(angle);
        }

        private void setRotateZ(double angle) {
            rotateZ.setAngle(angle);
        }
    }

    // Класс для представления атомов
    private static class Atom {
        private final String element;
        private double x;
        private final double y;
        private final double z;

        private Atom(String element, double x, double y, double z) {
            this.element = element;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getX() {
            return x;
        }

        public void setX(double newX) {
            this.x = newX;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }

        public double getRadius() {
            if (element.equals("C")) {
                return 2.0;
            } else if (element.equals("O")) {
                return 1.75;
            }

            return 0;
        }

        public Color getColor() {
            if (element.equals("C")) {
                return Color.GRAY;
            } else if (element.equals("O")) {
                return Color.RED;
            }
            return Color.GRAY;
        }
    }
}