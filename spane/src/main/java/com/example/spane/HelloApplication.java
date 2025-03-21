package com.example.spane;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class HelloApplication extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private ColorPicker colorPicker;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;

    @Override
    public void start(Stage primaryStage) {
        canvas = new Canvas(800, 500);
        gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setOnAction(e -> gc.setStroke(colorPicker.getValue()));

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()));

        Button addImageButton = new Button("Add Image");
        addImageButton.setOnAction(e -> loadImage(primaryStage));

        Button addMediaButton = new Button("Add Media");
        addMediaButton.setOnAction(e -> loadMedia(primaryStage));

        mediaView = new MediaView();

        HBox bottomContainer = new HBox(10, colorPicker, clearButton, addImageButton, addMediaButton);
        bottomContainer.setPadding(new Insets(10));

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            gc.beginPath();
            gc.moveTo(e.getX(), e.getY());
            gc.stroke();
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });

        // Wrap the mediaView in a StackPane to center it
        StackPane mediaStackPane = new StackPane();
        mediaStackPane.getChildren().add(mediaView);

        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        root.setBottom(bottomContainer);
        root.setRight(mediaStackPane);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Interactive Digital Whiteboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            javafx.scene.image.Image image = new javafx.scene.image.Image(file.toURI().toString());
            gc.drawImage(image, 50, 50);
        }
    }

    private void loadMedia(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Media Files", "*.mp4", "*.mp3"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

