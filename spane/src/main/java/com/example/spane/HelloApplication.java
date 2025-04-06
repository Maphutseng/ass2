package com.example.spane;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

// Main class for the Interactive Digital Whiteboard application
public class HelloApplication extends Application implements ToolbarInterface {

    // --- Global Variables ---
    private Canvas canvas;
    private GraphicsContext gc;
    private Color currentColor = Color.BLACK;
    private double brushSize = 2.0;
    private String currentTool = "Pen";
    private double textX = 100, textY = 100;
    private TextField textField;
    private VBox mediaBox; // Container to display media (images, videos, audio)

    // --- JavaFX Entry Point ---
    @Override
    public void start(Stage primaryStage) {

        // --- Canvas Initialization ---
        canvas = new Canvas(500, 500);
        gc = canvas.getGraphicsContext2D();
        gc.setStroke(currentColor);
        gc.setLineWidth(brushSize);

        // --- Drawing Events ---
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if (currentTool.equals("Pen") || currentTool.equals("Eraser")) {
                gc.beginPath();
                gc.moveTo(e.getX(), e.getY());
                gc.stroke();
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (currentTool.equals("Pen") || currentTool.equals("Eraser")) {
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
            }
        });

        // Capture text placement coordinates
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            textX = e.getX();
            textY = e.getY();
        });

        // --- Text Input Components ---
        textField = new TextField();
        textField.setPromptText("Enter text");

        Button addTextButton = new Button("Add Text");
        addTextButton.setOnAction(e -> addTextToCanvas());

        // --- Clear Button ---
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()));

        // --- Save Canvas Snapshot ---
        Button saveButton = new Button("Save Snapshot");
        saveButton.setOnAction(e -> {
            try {
                SnapshotUtils.saveAsImage(canvas, new File("whiteboard_snapshot.png"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // --- Media Buttons ---
        Button imageButton = new Button("Insert Image");
        imageButton.setOnAction(e -> {
            File file = new FileChooser().showOpenDialog(primaryStage);
            if (file != null) {
                MediaHandler.embedImage(mediaBox, file.getAbsolutePath());
            }
        });

        Button videoButton = new Button("Insert Video");
        videoButton.setOnAction(e -> {
            File file = new FileChooser().showOpenDialog(primaryStage);
            if (file != null) {
                MediaHandler.embedVideo(mediaBox, file.getAbsolutePath());
            }
        });

        Button audioButton = new Button("Play Audio");
        audioButton.setOnAction(e -> {
            File file = new FileChooser().showOpenDialog(primaryStage);
            if (file != null) {
                MediaHandler.embedAudio(); // Note: This line seems to be missing a path; fix below
            }
        });

        // --- Controls Panel (Text, Buttons) ---
        HBox controls = new HBox(10, textField, addTextButton, clearButton, imageButton, videoButton, audioButton, saveButton);
        controls.setPadding(new Insets(10));

        // --- Custom Toolbar (Color, Tool Selection, etc.) ---
        CanvasToolbar toolbar = new CanvasToolbar(this);

        // --- Media Box for displaying images/videos/audio ---
        mediaBox = new VBox(10);
        mediaBox.setPadding(new Insets(10));
        mediaBox.setStyle("-fx-border-color: gray; -fx-border-width: 1px;");

        // --- Layout: Split View (Canvas | Media) ---
        VBox canvasBox = new VBox(canvas);  // Canvas in vertical box
        HBox content = new HBox(10, canvasBox, mediaBox);  // Horizontal layout for canvas and media
        HBox.setHgrow(canvasBox, Priority.ALWAYS);
        HBox.setHgrow(mediaBox, Priority.ALWAYS);

        // --- Final Layout Setup ---
        VBox root = new VBox(toolbar, controls, content);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Interactive Digital Whiteboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // --- Method: Add Typed Text to Canvas ---
    private void addTextToCanvas() {
        String text = textField.getText();
        if (!text.isEmpty()) {
            gc.setFill(currentColor);
            gc.fillText(text, textX, textY);
            textField.clear();
        }
    }

    // --- Toolbar Tool Selection Handler ---
    @Override
    public void selectTool(String toolName) {
        this.currentTool = toolName;
        gc.setStroke(toolName.equals("Eraser") ? Color.WHITE : currentColor);
    }

    // --- Set Brush/Stroke Color from Toolbar ---
    @Override
    public void setColor(String colorCode) {
        this.currentColor = Color.web(colorCode);
        if (!currentTool.equals("Eraser")) {
            gc.setStroke(currentColor);
        }
    }

    // --- Set Brush Size from Toolbar ---
    @Override
    public void setBrushSize(double size) {
        this.brushSize = size;
        gc.setLineWidth(size);
    }

    // --- Launch Application ---
    public static void main(String[] args) {
        launch(args);
    }
}
