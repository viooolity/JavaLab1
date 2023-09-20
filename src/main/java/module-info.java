module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;

    requires com.dlsc.formsfx;
    requires javafx.swing;
    requires javafx.media;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}