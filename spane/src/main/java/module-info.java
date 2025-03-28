module com.example.spane {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.spane to javafx.fxml;
    exports com.example.spane;
}