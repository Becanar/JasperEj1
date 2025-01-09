module es.benatcano.jasperej1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jasperreports;
    requires java.sql; // Si usas JasperReports
    exports es.benatcano.jasperej1.app; // Exporta el paquete donde está la clase App
}
