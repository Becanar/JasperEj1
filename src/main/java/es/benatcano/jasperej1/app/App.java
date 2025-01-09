package es.benatcano.jasperej1.app;

import es.benatcano.jasperej1.app.db.ConectorDB;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class App extends Application {

    public static void main(String[] args) {
        launch(args); // Inicia la aplicación JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        ConectorDB db = null;
        try {
            // Se establece la conexión a la base de datos
            db = new ConectorDB();

            // Se carga el informe desde los recursos
            InputStream reportStream = db.getClass().getResourceAsStream("/es/benatcano/jasperej1/jasper/Paises.jasper");
            if (reportStream == null) {
                mostrarError("Error al cargar el informe", "El archivo Paises.jasper no se encontró en la ruta especificada.");
                System.exit(1); // Salir de la aplicación si no se encuentra el archivo
                return; // Aunque `System.exit(1)` termina la aplicación, por seguridad lo incluyo
            }

            // Se carga el informe JasperReport
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);

            // Se preparan los parámetros, incluyendo la ruta de las imágenes
            Map<String, Object> parameters = new HashMap<>();
// Verificación de la ruta de la carpeta de imágenes
            URL imagePathURL = db.getClass().getResource("/es/benatcano/jasperej1/img/");
            if (imagePathURL == null) {
                mostrarError("Error en la ruta de las imágenes", "La carpeta de imágenes no se encuentra en el recurso.");
                System.exit(1);
            }
            parameters.put("ImagePath", imagePathURL.toString());


            // Se rellena el informe con los datos de la base de datos y los parámetros
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, db.getConnection());

            // Se muestra el informe en un visor de JasperReports
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setVisible(true);

        } catch (SQLException e) {
            mostrarError("Error en la base de datos", "No se pudo conectar a la base de datos: " + e.getMessage());
            e.printStackTrace(); // Para depuración en consola
            System.exit(1); // Termina la ejecución en caso de error de base de datos
        } catch (JRException e) {
            mostrarError("Error al generar el informe", "No se pudo generar el informe: " + e.getMessage());
            e.printStackTrace(); // Para depuración en consola
            System.exit(1); // Termina la ejecución en caso de error con JasperReports
        } catch (Exception e) {
            mostrarError("Error inesperado", "Ocurrió un error inesperado: " + e.getMessage());
            e.printStackTrace(); // Para depuración en consola
            System.exit(1); // Termina la ejecución en caso de un error inesperado
        } finally {
            // Cerrar la conexión a la base de datos si fue abierta
            if (db != null) {
                try {
                    db.closeConexion();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
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
