/*
 * Nom du fichier: DrawerOpenController.java
 * Auteur: TEUGUIA TADJUIDJE Rodolf Séderis
 * Description: Contrôleur pour l'ouverture de fichiers dans l'application de dessin.
 */

package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class DrawerOpenController {
    // Éléments graphiques déclarés dans le fichier FXML
    @FXML
    private TextField folderLocation;

    @FXML
    private ChoiceBox<String> recentFile;

    @FXML
    private Button btnFolder;

    @FXML
    private CheckBox chbxLoadType;

    // Fichier sélectionné pour l'ouverture
    private File fileOpen;

    // Méthode d'initialisation appelée lors de la création du contrôleur
    @FXML
    private void initialize() {
        // Initialisation des éléments graphiques et des écouteurs d'événements

        recentFile.getItems().addAll(DrawerMainController.recentFiles);

        recentFile.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                fileOpen = new File(newValue);
            }
        });

        chbxLoadType.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                // choisir comment ouvrir le fichier (directement depuis le disque ou depuis les fichiers recement ouverts)
                if (newValue){
                    btnFolder.setDisable(false);
                    recentFile.setDisable(true);

                    if (folderLocation.getText() != null) {
                        fileOpen = new File(folderLocation.getText());
                    }
                } else {
                    btnFolder.setDisable(true);
                    recentFile.setDisable(false);

                    if (recentFile.getSelectionModel().getSelectedItem() != null) {
                        fileOpen = new File(recentFile.getSelectionModel().getSelectedItem());
                    }
                }
            }
        });
    }

    // Méthode appelée lors du chargement d'un fichier récemment utilisé
    @FXML
    void LoadRecent(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export File");
        File homeDir = new File(System.getProperty("user.home"));
        fileChooser.setInitialDirectory(homeDir);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Drawer File", "*.drw"));

        fileOpen = fileChooser.showOpenDialog(new Stage());
        if (fileOpen != null) {
            folderLocation.setText(fileOpen.getPath());
        }
    }

    // Méthode permettant d'obtenir le fichier sélectionné pour l'ouverture
    public File GetFileOpen(){
        return fileOpen;
    }
}
