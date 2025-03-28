package com.example.spane;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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
    private TextField textField;
    private Label mediaLabel;
    private Button playButton, pauseButton, stopButton;
    private StackPane mediaContainer;
    private double textX = 100, textY = 100;

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

        textField = new TextField();
        textField.setPromptText("Enter text here");

        Button addTextButton = new Button("Add Text");
        addTextButton.setOnAction(e -> addTextToCanvas());

        mediaView = new MediaView();
        mediaLabel = new Label("No media selected");

        playButton = new Button("Play");
        pauseButton = new Button("Pause");
        stopButton = new Button("Stop");

        playButton.setDisable(true);
        pauseButton.setDisable(true);
        stopButton.setDisable(true);

        playButton.setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.play();
            }
        });

        pauseButton.setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        });

        stopButton.setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
        });

        HBox topContainer = new HBox(10, playButton, pauseButton, stopButton, addMediaButton);
        topContainer.setPadding(new Insets(10));
        topContainer.setStyle("-fx-background-color: lightgray; -fx-border-color: black; -fx-border-width: 2px;");

        HBox bottomContainer = new HBox(10, colorPicker, clearButton, addImageButton, textField, addTextButton);
        bottomContainer.setPadding(new Insets(10));
        bottomContainer.setStyle("-fx-background-color: lightgray; -fx-border-color: black; -fx-border-width: 2px;");

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            gc.beginPath();
            gc.moveTo(e.getX(), e.getY());
            gc.stroke();
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            textX = e.getX();
            textY = e.getY();
        });

        mediaContainer = new StackPane();
        mediaContainer.setMinSize(400, 300);
        mediaContainer.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
        mediaContainer.getChildren().add(mediaView);

        VBox mediaControls = new VBox(10, mediaLabel, mediaContainer);
        mediaControls.setPadding(new Insets(10));
        mediaControls.setMaxWidth(Double.MAX_VALUE);
        mediaControls.setMaxHeight(Double.MAX_VALUE);

        BorderPane root = new BorderPane();
        root.setTop(topContainer);
        root.setBottom(bottomContainer);

        // SplitPane for equal resizing of media and whiteboard
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(canvas, mediaControls);
        splitPane.setDividerPositions(0.5);
        root.setCenter(splitPane);

        Scene scene = new Scene(root, 1000, 600);
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
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }

            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            mediaLabel.setText("Playing: " + file.getName());

            playButton.setDisable(false);
            pauseButton.setDisable(false);
            stopButton.setDisable(false);

            mediaView.setVisible(true);
            mediaContainer.setMinSize(400, 300);

            // Bind video size to container
            mediaView.fitWidthProperty().bind(mediaContainer.widthProperty());
            mediaView.fitHeightProperty().bind(mediaContainer.heightProperty());
            mediaView.setPreserveRatio(true);

            if (file.getName().endsWith(".mp3")) {
                mediaView.setVisible(false);
                mediaContainer.setMinSize(0, 0);
            } else {
                mediaView.setVisible(true);
                mediaContainer.setMinSize(400, 300);
            }

            mediaPlayer.setOnEndOfMedia(() -> {
                mediaLabel.setText("Media finished: " + file.getName());
                mediaPlayer.stop();
            });

            mediaPlayer.play();
        }
    }

    private void addTextToCanvas() {
        String text = textField.getText();
        if (!text.isEmpty()) {
            gc.setFill(colorPicker.getValue());
            gc.fillText(text, textX, textY);
            textField.clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
