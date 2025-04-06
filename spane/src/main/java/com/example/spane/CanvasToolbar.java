package com.example.spane;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class CanvasToolbar extends HBox {
    private final ToolbarInterface toolHandler;

    public CanvasToolbar(ToolbarInterface handler) {
        this.toolHandler = handler;
        initToolbar();
    }

    private void initToolbar() {
        Button pen = new Button("Pen");
        pen.setOnAction(e -> toolHandler.selectTool("Pen"));

        Button eraser = new Button("Eraser");
        eraser.setOnAction(e -> toolHandler.selectTool("Eraser"));

        Button text = new Button("Text");
        text.setOnAction(e -> toolHandler.selectTool("Text"));

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setOnAction(e -> toolHandler.setColor(String.format("#%02X%02X%02X",
                (int) (colorPicker.getValue().getRed() * 255),
                (int) (colorPicker.getValue().getGreen() * 255),
                (int) (colorPicker.getValue().getBlue() * 255))));

        Slider brushSize = new Slider(1, 20, 2);
        brushSize.valueProperty().addListener((obs, oldVal, newVal) ->
                toolHandler.setBrushSize(newVal.doubleValue())
        );

        this.setSpacing(10);
        this.getChildren().addAll(pen, eraser, text, colorPicker, brushSize);
    }
}
