module com.example.mvc1flight {
    requires javafx.controls;
    requires javafx.fxml;

    opens view to javafx.fxml;
    exports view;
}
