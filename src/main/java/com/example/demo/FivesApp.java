package com.example.demo;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Optional;

public class FivesApp extends Application {

    private final ObservableList<DataItem> data = FXCollections.observableArrayList(
            new DataItem("Си", "Деннис Ритчи", 1972),
            new DataItem("C++", "Бьерн Страуструп", 1983),
            new DataItem("Python", "Гвидо ван Россум", 1991),
            new DataItem("Java", "Джеймс Гослинг", 1995),
            new DataItem("JavaScript", "Брендон Айк", 1995),
            new DataItem("C#", "Андерс Хейлсберг", 2001),
            new DataItem("Scala", "Мартин Одерски", 2003)
    );

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        TableView<DataItem> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<DataItem, String> languageCol = new TableColumn<>("Язык");
        languageCol.setCellValueFactory(new PropertyValueFactory<>("language"));

        TableColumn<DataItem, String> authorCol = new TableColumn<>("Автор");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<DataItem, Integer> yearCol = new TableColumn<>("Год");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        tableView.getColumns().addAll(languageCol, authorCol, yearCol);
        tableView.setItems(data);

        MenuBar menuBar = createMenuBar(tableView);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setCenter(tableView);

        Scene scene = new Scene(borderPane, 600, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Таблица данных");
        primaryStage.show();
    }

    private MenuBar createMenuBar(TableView<DataItem> tableView) {
        MenuBar menuBar = new MenuBar();

        // Добавление
        Menu addMenu = new Menu("Добавить");
        MenuItem addItem = new MenuItem("Новая строка");
        addItem.setOnAction(event -> addRow(tableView));
        addMenu.getItems().add(addItem);

        MenuItem addColumnItem = new MenuItem("Добавить столбец");
        addColumnItem.setOnAction(event -> addColumn(tableView));
        addMenu.getItems().add(addColumnItem);

        // Изменение
        Menu editMenu = new Menu("Изменить");
        MenuItem editItem = new MenuItem("Редактировать строку");
        editItem.setOnAction(event -> editRow(tableView));
        editMenu.getItems().add(editItem);

        // Показывать/скрывать столбцы
        Menu showHideMenu = new Menu("Столбцы");
        for (TableColumn<DataItem, ?> column : tableView.getColumns()) {
            CheckMenuItem item = new CheckMenuItem(column.getText());
            item.setSelected(true);
            item.setOnAction(event -> column.setVisible(item.isSelected()));
            showHideMenu.getItems().add(item);
        }

        menuBar.getMenus().addAll(addMenu, editMenu, showHideMenu);
        return menuBar;
    }

    private void addRow(TableView<DataItem> tableView) {
        DataItem newItem = new DataItem("Новый язык", "Новый автор", 0);
        tableView.getItems().add(newItem);
        tableView.scrollTo(newItem);
    }

    private void addColumn(TableView<DataItem> tableView) {
        // Создайте новый TableColumn и добавьте его к таблице
        TableColumn<DataItem, ?> newColumn = new TableColumn<>("Новый столбец");
        tableView.getColumns().add(newColumn);
    }

    private void editRow(TableView<DataItem> tableView) {
        DataItem selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Отобразите диалог редактирования и обновите данные после его закрытия
            boolean okClicked = showEditDialog(selectedItem);
            if (okClicked) {
                // Пользователь подтвердил изменения, обновите данные
                tableView.refresh();
            }
        }
    }

    private boolean showEditDialog(DataItem item) {
        // Создаем диалоговое окно редактирования
        Dialog<DataItem> dialog = new Dialog<>();
        dialog.setTitle("Редактировать строку");

        // Устанавливаем тип кнопок (OK и Отмена)
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Создаем форму редактирования
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField languageField = new TextField();
        languageField.setPromptText("Язык");
        languageField.setText(item.getLanguage());

        TextField authorField = new TextField();
        authorField.setPromptText("Автор");
        authorField.setText(item.getAuthor());

        TextField yearField = new TextField();
        yearField.setPromptText("Год");
        yearField.setText(String.valueOf(item.getYear())); // Устанавливаем значение года

        grid.add(new Label("Язык:"), 0, 0);
        grid.add(languageField, 1, 0);
        grid.add(new Label("Автор:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Год:"), 0, 2); // Добавляем поле "Год"
        grid.add(yearField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Ожидаем закрытия диалога и возвращаем результат
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                item.setLanguage(languageField.getText());
                item.setAuthor(authorField.getText());
                try {
                    int year = Integer.parseInt(yearField.getText()); // Пытаемся получить значение года
                    item.setYear(year);
                } catch (NumberFormatException e) {
                    // В случае некорректного ввода числа года, можно обработать ошибку здесь.
                    // Например, вывести сообщение пользователю.
                }
                return item;
            }
            return null;
        });

        Optional<DataItem> result = dialog.showAndWait();
        return result.isPresent();
    }

    public static class DataItem {
        private final StringProperty language;
        private final StringProperty author;
        private int year;

        public DataItem(String language, String author, int year) {
            this.language = new SimpleStringProperty(language);
            this.author = new SimpleStringProperty(author);
            this.year = year;
        }

        public String getLanguage() {
            return language.get();
        }

        public void setLanguage(String language) {
            this.language.set(language);
        }

        public StringProperty languageProperty() {
            return language;
        }

        public String getAuthor() {
            return author.get();
        }

        public void setAuthor(String author) {
            this.author.set(author);
        }

        public StringProperty authorProperty() {
            return author;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }
    }
}
