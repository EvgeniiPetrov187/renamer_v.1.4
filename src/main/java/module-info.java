module com.example.renamer3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.renamer3 to javafx.fxml;
    exports com.example.renamer3;
}