module es.benatcano.jasperej1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens es.benatcano.jasperej1 to javafx.fxml;
    exports es.benatcano.jasperej1;
}