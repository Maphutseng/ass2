module com.example.spane {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;
    requires javafx.swing; // Required for SwingFXUtils and ImageIO

    opens com.example.spane to javafx.fxml;
    exports com.example.spane;
}
