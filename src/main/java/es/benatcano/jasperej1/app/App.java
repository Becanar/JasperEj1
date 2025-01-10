package es.benatcano.jasperej1.app;

import es.benatcano.jasperej1.app.db.ConectorDB;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class App extends Application {

    public static void main(String[] args) {
        launch(args); // Inicia la aplicación JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        ConectorDB db;
        try {
            // Se establece la conexión a la base de datos
            db = new ConectorDB();

            // Se carga el informe desde los recursos
            InputStream reportStream = db.getClass().getResourceAsStream("/es/benatcano/jasperej1/jasper/Paises.jasper");
            if (reportStream == null) {
                System.out.println("El archivo Coffee.jrxml no se encontró.");
                return; // Salimos si no encontramos el archivo
            }
            System.out.println(db.getClass().getResource("/es/benatcano/jasperej1/img/").toString());

            // Se carga el informe JasperReport
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);

            // Parámetros para el informe
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("IMAGE_PATH", db.getClass().getResource("/es/benatcano/jasperej1/img/").toString());

            // Se rellena el informe con los datos de la base de datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, db.getConnection());

            // Se muestra el informe en un visor de JasperReports
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setVisible(true);

        } catch (SQLException | JRException e) {
            e.printStackTrace(); // Para depuración en consola
            mostrarError("Error en la base de datos", "No se pudo conectar a la base de datos o generar el informe.");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



    private void mostrarError(String titulo, String mensaje) {
        // Crear una ventana emergente de tipo "error"
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null); // No queremos un encabezado
        alert.setContentText(mensaje); // El mensaje que queremos mostrar
        alert.showAndWait(); // Mostrar el mensaje y esperar a que el usuario lo cierre
    }
}
