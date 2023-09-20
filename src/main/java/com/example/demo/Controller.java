package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    private LineChart<Double, Double> chart;

    @FXML
    private CheckBox function1Checkbox;

    @FXML
    private CheckBox function2Checkbox;

    @FXML
    private TextField minTextField;

    @FXML
    private TextField maxTextField;

    @FXML
    private TextField stepTextField;

    private double min;
    private double max;
    private double step;

    @FXML
    private void onFunction1Selected() {
        updateChart(); onMinValueChanged();
    }

    @FXML
    private void onFunction2Selected() {
        updateChart(); onMinValueChanged();
    }

    private void onMinValueChanged() {
        min = Double.parseDouble(minTextField.getText());
        updateChart();
    }

    @FXML
    private void onMaxValueChanged() {
        max = Double.parseDouble(maxTextField.getText());
        updateChart();
    }

    @FXML
    private void onStepValueChanged() {
        step = Double.parseDouble(stepTextField.getText());
        System.out.println(step);
        updateChart();
    }


    private void updateChart() {
        chart.getData().clear();

        if (function1Checkbox.isSelected()) {
            XYChart.Series<Double, Double> function1Series = new XYChart.Series<>();
            function1Series.setName("Sin");

            for (double x = min; x <= max; x += step) {
                double y = Math.sin(x);
                function1Series.getData().add(new XYChart.Data<>(x, y));
            }
            chart.getData().add(function1Series);
        }

        if (function2Checkbox.isSelected()) {
            XYChart.Series<Double, Double> function2Series = new XYChart.Series<>();
            function2Series.setName("Cos");

            for (double x = min; x <= max; x += step) {
                double y = Math.cos(x);
                function2Series.getData().add(new XYChart.Data<>(x, y));
            }
            chart.getData().add(function2Series);
        }
    }

    public void initialize() {
        minTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            onMinValueChanged();
        });
        maxTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            onMaxValueChanged();
        });
        stepTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            onStepValueChanged();
        });
    }
}
