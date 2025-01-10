package es.benatcano.jasperej1.app;

import es.benatcano.jasperej1.app.db.ConectorDB;
import javafx.application.Application;
import javafx.application.Platform;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * La clase principal que arranca la aplicación JavaFX y genera el informe usando JasperReports.
 */
public class App extends Application {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Método que inicializa la aplicación JavaFX y genera el informe.
     * @param primaryStage El escenario principal de la aplicación.
     */
    @Override
    public void start(Stage primaryStage) {
        ConectorDB db;
        try {
            db = new ConectorDB();

            InputStream reportStream = db.getClass().getResourceAsStream("/es/benatcano/jasperej1/jasper/Paises.jasper");
            if (reportStream == null) {
                LOGGER.log(Level.SEVERE, "El archivo Paises.jasper no se encontró.");
                return;
            }

            LOGGER.log(Level.INFO, "Ruta de la imagen: {0}", db.getClass().getResource("/es/benatcano/jasperej1/img/").toString());

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("IMAGE_PATH", db.getClass().getResource("/es/benatcano/jasperej1/img/").toString());

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, db.getConnection());

            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setVisible(true);

            // Evento para cuando se cierra la ventana
            primaryStage.setOnCloseRequest(event -> {
                LOGGER.log(Level.INFO, "Ventana cerrada. Cerrando la aplicación.");
                Platform.exit();  // Cierra la aplicación completamente
            });

        } catch (SQLException | JRException e) {
            LOGGER.log(Level.SEVERE, "Error al generar el informe o conectar a la base de datos.", e);
            mostrarError("Error en la base de datos", "No se pudo conectar a la base de datos o generar el informe.");
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Archivo no encontrado.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Muestra una ventana emergente con un mensaje de error.
     * @param titulo El título de la ventana emergente.
     * @param mensaje El mensaje de error a mostrar.
     */
    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
