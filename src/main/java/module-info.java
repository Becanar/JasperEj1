module es.benatcano.jasperej1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires net.sf.jasperreports.core; // Si usas JasperReports
    exports es.benatcano.jasperej1.app; // Exporta el paquete donde está la clase App
}
