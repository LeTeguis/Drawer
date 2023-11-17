/*
 * Nom du fichier: DrawerCreateController.java
 * Auteur: TEUGUIA TADJUIDJE Rodolf Séderis
 * Description: Contrôleur JavaFX pour la création d'un nouveau dessin.
 */

package controller;

import graphics.maths.Vector2D;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.function.UnaryOperator;

public class DrawerCreateController {
    File file;

    // Emplacement du dossier du projet en cours
    @FXML
    private TextField folderLocation;

    // Spinner pour la hauteur du fichier en pixels
    @FXML
    private Spinner<Integer> sizeHeight;

    // Spinner pour la largeur du fichier en pixels
    @FXML
    private Spinner<Integer> sizeWidth;

    @FXML
    private void initialize() {
        // Initialisation des formateurs de texte pour les spinners de hauteur et de largeur
        TextFormatter<Integer> textFormatterHeight = new TextFormatter<>(new IntegerStringConverter(), 512, new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                NumberFormat numberFormat = NumberFormat.getIntegerInstance();
                ParsePosition position = new ParsePosition(0);
                Object obj = numberFormat.parseObject(change.getControlNewText(), position);

                if (obj != null && position.getIndex() == change.getControlNewText().length()) {
                    return change;
                }
                return null;
            }
        });

        TextFormatter<Integer> textFormatterWidth = new TextFormatter<>(new IntegerStringConverter(), 512, new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                NumberFormat numberFormat = NumberFormat.getIntegerInstance();
                ParsePosition position = new ParsePosition(0);
                Object obj = numberFormat.parseObject(change.getControlNewText(), position);

                if (obj != null && position.getIndex() == change.getControlNewText().length()) {
                    return change;
                }
                return null;
            }
        });

        // Configuration des spinners avec des valeurs par défaut et des formateurs de texte
        sizeHeight.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 8192, 512));
        sizeHeight.getEditor().setTextFormatter(textFormatterHeight);
        sizeWidth.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 8192, 512));
        sizeWidth.getEditor().setTextFormatter(textFormatterWidth);
    }

    @FXML
    void ShooseFolderLocation(ActionEvent event) {
        // Dialogue de choix de dossier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("New Drawer");
        File homeDir = new File(System.getProperty("user.home"));
        fileChooser.setInitialDirectory(homeDir);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Drawer Files", "*.drw"));

        // Affichage de la boîte de dialogue et mise à jour de l'emplacement du dossier
        File selectedFile = fileChooser.showSaveDialog(new Stage());
        if (selectedFile != null) {
            file = selectedFile;
            folderLocation.setText(selectedFile.getAbsolutePath());
        }
    }

    // Vérifie la validité des informations saisies
    public boolean IsValid() {
        String fileNameText = file.getName().trim();
        String folderLocationText = folderLocation.getText().trim();

        if (fileNameText.isEmpty() || fileNameText.isBlank() ||
                sizeHeight.getValue() < 10 || sizeWidth.getValue() < 10 ||
                folderLocationText.isEmpty() || folderLocationText.isBlank()) {
            return false;
        }

        return true;
    }

    // Récupère le nom du fichier
    public String GetFileName() {
        return (file == null) ? null : file.getName();
    }

    // Récupère l'emplacement du dossier
    public String GetFolderLocation() {
        return folderLocation.getText();
    }

    // Récupère la taille du fichier
    public Vector2D GetSize() {
        return new Vector2D(sizeWidth.getValue(), sizeHeight.getValue());
    }

    // Récupère l'objet File du fichier
    public File GetFile() {
        return file;
    }
}
