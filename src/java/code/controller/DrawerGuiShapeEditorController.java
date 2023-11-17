/*
 * Nom du fichier: DrawerGuiShapeEditorController.java
 * Auteur: TEUGUIA TADJUIDJE Rodolf Séderis
 * Description: Contrôleur JavaFX pour l'édition d'une forme dans un éditeur graphique.
 */

package controller;

import graphics.AShape;
import graphics.maths.Angle;
import graphics.ShapeOutlinerEditor;
import graphics.maths.Vector2D;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.function.UnaryOperator;

public class DrawerGuiShapeEditorController {
    @FXML
    private TextField editArea;

    @FXML
    private TextField editCCR;

    @FXML
    private TextField editICR;

    @FXML
    private TextField editPerimeter;

    @FXML
    private Spinner<Integer> editPositionX;

    @FXML
    private Spinner<Integer> editPositionY;

    @FXML
    private Spinner<Integer> editSizeH;

    @FXML
    private Spinner<Integer> editSizeW;
    @FXML
    private Spinner<Integer> editRotation;

    @FXML
    private CheckBox editStrock;

    @FXML
    private ColorPicker editStrockColor;

    @FXML
    private Spinner<Integer> editStrockSize;

    @FXML
    private CheckBox editFill;

    @FXML
    private ColorPicker editFillColor;

    private ActualizeRender actualizeRender;

    @FXML
    private void initialize() {
        // Initialisation du contrôleur
    }

    public void SetActualizeRender(ActualizeRender actualizeRender){
        this.actualizeRender = actualizeRender;
    }

    private TextFormatter<Integer> GetTextFormatter(int defaultValue, int index){
        // Méthode pour obtenir un formateur de texte avec une valeur par défaut et un index spécifié

        TextFormatter<Integer> textFormatter = new TextFormatter<Integer>(new IntegerStringConverter(), defaultValue, new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                NumberFormat numberFormat = NumberFormat.getIntegerInstance();
                ParsePosition position = new ParsePosition(index);
                Object obj = numberFormat.parseObject(change.getControlNewText(), position);

                if (obj != null && position.getIndex() == change.getControlNewText().length()){
                    return change;
                }
                return null;
            }
        });
        return textFormatter;
    }

    private void SetSpinerEdit(Spinner<Integer> spinner, int min, int max, int value, PropertieChange change){
        // Méthode pour configurer un Spinner avec des valeurs par défaut, un min, un max et un écouteur de changement

        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, value));
        spinner.getEditor().setTextFormatter(GetTextFormatter(value, 0));

        spinner.getValueFactory().valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                if (change != null){
                    change.Change(newValue);
                }
            }
        });
    }

    public void SetShapeToEdit(AShape shape, ShapeOutlinerEditor shapeOutliner, int w, int h){
        // Méthode pour configurer l'édition d'une forme avec des Spinners et des CheckBox pour les propriétés de la forme

        if (shape == null) return;

        SetSpinerEdit(editPositionX, -w, w, (int) shape.GetPosition().x, newValue -> {
            shape.SetPosition(new Vector2D(newValue, shape.GetPosition().y));
            SetValueFixet(shape);
            ARender();
        });

        SetSpinerEdit(editPositionY, -h, h, (int) shape.GetPosition().y, newValue -> {
            shape.SetPosition(new Vector2D(shape.GetPosition().x, newValue));
            SetValueFixet(shape);
            ARender();
        });

        SetSpinerEdit(editSizeH, 0, h, (int) shape.GetSize().y, newValue -> {
            shape.SetSize(new Vector2D(shape.GetSize().x, newValue));
            SetValueFixet(shape);
            ARender();
        });

        SetSpinerEdit(editSizeW, 0, w, (int) shape.GetSize().x, newValue -> {
            shape.SetSize(new Vector2D(newValue, shape.GetSize().y));
            SetValueFixet(shape);
            ARender();
        });

        SetSpinerEdit(editRotation, 0, 360, (int) shape.GetAngle().GetDegree(), newValue -> {
            shape.SetAngle(Angle.FromDegree(newValue));
            ARender();
        });

        SetValueFixet(shape);

        editStrock.setSelected(shape.IsHasStrock());
        editStrock.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                shape.SetHasStrock(newValue);

                editStrockColor.setDisable(!newValue);
                editStrockSize.setDisable(!newValue);

                shapeOutliner.IsVisible().setValue(shape.IsEnable());
                ARender();
            }
        });

        editStrockColor.setValue(shape.GetStrockColor());
        editStrockColor.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                shape.SetStrockColor(newValue);
                ARender();
            }
        });

        SetSpinerEdit(editStrockSize, 0, 1000, (int) shape.GetStrock(), newValue -> {
            shape.SetStrock(newValue);
            ARender();
        });

        editFill.setSelected(shape.IsHasStrock());
        editFill.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                shape.SetHasFill(newValue);
                editFillColor.setDisable(!newValue);
                shapeOutliner.IsVisible().setValue(shape.IsEnable());
                ARender();
            }
        });

        editFillColor.setValue(shape.GetFillColor());
        editFillColor.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                shape.SetFillColor(newValue);
                ARender();
            }
        });
    }

    private void SetValueFixet(AShape shape){
        // Méthode pour définir les valeurs fixes (qui ne son pas modifier directement par l'utilisateur) dans les champs de texte en fonction des propriétés de la forme
        editArea.setText("" + shape.CalculerAire());
        editCCR.setText("" + shape.CalculerRayonCercleEnglobant());
        editICR.setText("" + shape.CalculerRayonCercleEnglober());
        editPerimeter.setText("" + shape.CalculerPerimetre());
    }

    public void SetShapeToEdit(AShape shape, ShapeOutlinerEditor shapeOutliner, Vector2D size) {
        // Surcharge de la méthode SetShapeToEdit pour prendre en compte la taille de la forme en utilisant un Vector2D
        SetShapeToEdit(shape, shapeOutliner, (int) size.x, (int) size.y);
    }

    private void ARender(){
        // Méthode pour déclencher le rendu lorsque des propriétés sont modifiées
        if (actualizeRender != null){
            actualizeRender.Actualize();
        }
    }

    private interface PropertieChange{
        // Interface fonctionnelle pour le changement de propriété de la forme selectionner
        void Change(int newValue);
    }

    public interface ActualizeRender{
        // Interface fonctionnelle pour l'actualisation du rendu
        void Actualize();
    }
}
