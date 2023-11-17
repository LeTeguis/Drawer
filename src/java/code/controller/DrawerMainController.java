/*
 * Nom du fichier: DrawerMainController.java
 * Auteur: TEUGUIA TADJUIDJE Rodolf Séderis
 * Description: Contrôleur principal de l'application de dessin.
 */

package controller;

import graphics.*;
import graphics.maths.Vector2D;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DrawerMainController{
    // Déclaration des éléments graphiques
    @FXML
    private ToggleButton tgCircle;
    @FXML
    private ToggleButton tgElipse;
    @FXML
    private ToggleButton tgHexagone;
    @FXML
    private ToggleButton tgLosange;
    @FXML
    private ToggleButton tgRectangle;
    @FXML
    private ToggleButton tgSelect;
    @FXML
    private ToggleButton tgSquare;
    @FXML
    private ScrollPane scrollProperties;

    // Page
    @FXML
    private TabPane tbAllPages;

    @FXML
    private TableColumn<ShapeOutlinerEditor, Boolean> outlinerLocked;

    @FXML
    private TableColumn<ShapeOutlinerEditor, String> outlinerName;

    @FXML
    private TableColumn<ShapeOutlinerEditor, Boolean> outlinerVisible;

    @FXML
    private TableView<ShapeOutlinerEditor> pageOutliner;

    @FXML
    private Button idSaveCurrent;
    @FXML
    private Button btnExport;

    @FXML
    private ColorPicker btnBackgrounColor;
    @FXML
    private TextField editSizeHPage;
    @FXML
    private TextField editSizeWPage;

    // Déclaration des variables
    int currentTab = -1;
    RendererPage currentRenderPage = null;
    public static List<String> recentFiles = new ArrayList<String>();
    public static List<String> currentOpenFiles = new ArrayList<String>();

    @FXML
    private void initialize() {
        // Initialisation du contrôleur
        ToggleGroup tgGroup = new ToggleGroup();
        tgCircle.setToggleGroup(tgGroup);
        tgElipse.setToggleGroup(tgGroup);
        tgHexagone.setToggleGroup(tgGroup);
        tgLosange.setToggleGroup(tgGroup);
        tgRectangle.setToggleGroup(tgGroup);
        tgSelect.setToggleGroup(tgGroup);
        tgSquare.setToggleGroup(tgGroup);

        // Ajout des écouteurs d'événements
        btnBackgrounColor.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                if (currentTab == -1) return;
                ((RendererPage)((ScrollPane)tbAllPages.getTabs().get(currentTab).getContent()).getContent()).SetBackgroundColor(newValue);
            }
        });

        outlinerVisible.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ShapeOutlinerEditor, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<ShapeOutlinerEditor, Boolean> param) {
                return param.getValue().IsVisible();
            }
        });

        outlinerLocked.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ShapeOutlinerEditor, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<ShapeOutlinerEditor, Boolean> param) {
                return param.getValue().IsLocked();
            }
        });

        outlinerName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ShapeOutlinerEditor, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ShapeOutlinerEditor, String> param) {
                return param.getValue().GetName();
            }
        });

        tbAllPages.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println(newValue);
                currentTab = (int) newValue;

                Tab tbPage = tbAllPages.getTabs().get((Integer) newValue);
                tbPage.setClosable(true);

                ScrollPane scrollPane = (ScrollPane) tbPage.getContent();
                Center(scrollPane.getViewportBounds());
                scrollPane.viewportBoundsProperty().addListener((scr_observable, scr_oldValue, scr_newValue) -> {
                    Center(scr_newValue);
                });

                RendererPage rendererPage = (RendererPage)scrollPane.getContent();

                editSizeHPage.setText("" + rendererPage.GetSize().y);
                editSizeWPage.setText("" + rendererPage.GetSize().x);
                btnBackgrounColor.setValue(rendererPage.GetBackgroundColor());

                pageOutliner.getItems().clear();

                for (int i = 0; i < rendererPage.GetShapeSize(); i++){
                    // pageOutliner.getItems().add(new ShapeOutlinerEditor(rendererPage.GetShape(i), i));
                    pageOutliner.getItems().add(i, new ShapeOutlinerEditor(rendererPage.GetShape(i), i));
                }

                pageOutliner.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ShapeOutlinerEditor>() {
                    @Override
                    public void changed(ObservableValue<? extends ShapeOutlinerEditor> observable, ShapeOutlinerEditor oldValue, ShapeOutlinerEditor newValue) {
                        if (newValue != null){
                            rendererPage.Selected(newValue.GetIndex());
                        }
                    }
                });
            }
        });

        LoadRecentFiles();
    }

    private void DefaultActionMode(EActionMode actionMode){
        if (actionMode == EActionMode.Selection) tgSelect.setSelected(true);
        else if (actionMode == EActionMode.CreateLosange) tgLosange.setSelected(true);
        else if (actionMode == EActionMode.CreateHexagone) tgHexagone.setSelected(true);
        else if (actionMode == EActionMode.CreateCircle) tgCircle.setSelected(true);
        else if (actionMode == EActionMode.CreateRectangle) tgRectangle.setSelected(true);
        else if (actionMode == EActionMode.CreateEllipse) tgElipse.setSelected(true);
        else if (actionMode == EActionMode.CreateSquare) tgSquare.setSelected(true);
    }

    // Méthodes liées aux actions utilisateur
    @FXML
    void NewFile(ActionEvent event) throws IOException {
        Alert createFile = new Alert(Alert.AlertType.NONE);
        createFile.setTitle("Create new drawing page");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/drawer_create.fxml"));
        VBox root = loader.load();
        DrawerCreateController drawerCreateController = loader.getController();
        createFile.getDialogPane().setContent(root);

        ButtonType btnCreate = new ButtonType("Create");
        ButtonType btnCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        createFile.getButtonTypes().setAll(btnCreate, btnCancel);

        Optional<ButtonType> choice = createFile.showAndWait();
        if (choice.get() == btnCreate) {
            if (drawerCreateController.IsValid()){
                for (String f : recentFiles){
                    if (f.equals(drawerCreateController.GetFile().getPath())){
                        return;
                    }
                }

                Vector2D size = drawerCreateController.GetSize();
                RendererPage page = new RendererPage(drawerCreateController.GetFile(), drawerCreateController.GetFolderLocation(), size);

                CreateOrAddRendererPage(page);

                Save(page, drawerCreateController.GetFile());
            }else{
                System.out.println("Invalid value");
            }
        }
        else if (choice.get() == btnCancel) {
            System.out.println("Cancel");
        }
    }

    // Méthode pour centrer la page dans le ScrollPane
    private void Center(Bounds viewPortBounds) {
        if (currentTab == -1){
            return;
        }

        RendererPage page = (RendererPage)((ScrollPane)tbAllPages.getTabs().get(currentTab).getContent()).getContent();

        double width = viewPortBounds.getWidth();
        double height = viewPortBounds.getHeight();
        if (width > page.GetSize().x) {
            page.setTranslateX((width - page.GetSize().x) / 2);
        } else {
            page.setTranslateX(0);
        }
        if (height > page.GetSize().y) {
            page.setTranslateY((height - page.GetSize().y) / 2);
        } else {
            page.setTranslateY(0);
        }

    }

    @FXML
    void ACircle(ActionEvent event) {
        if (currentTab == -1) return;
        ((RendererPage)((ScrollPane)tbAllPages.getTabs().get(currentTab).getContent()).getContent()).SetActionMode(EActionMode.CreateCircle);
    }

    @FXML
    void AElipse(ActionEvent event) {
        if (currentTab == -1) return;
        ((RendererPage)((ScrollPane)tbAllPages.getTabs().get(currentTab).getContent()).getContent()).SetActionMode(EActionMode.CreateEllipse);
    }

    @FXML
    void AHexagone(ActionEvent event) {
        if (currentTab == -1) return;
        ((RendererPage)((ScrollPane)tbAllPages.getTabs().get(currentTab).getContent()).getContent()).SetActionMode(EActionMode.CreateHexagone);
    }

    @FXML
    void ALosange(ActionEvent event) {
        if (currentTab == -1) return;
        ((RendererPage)((ScrollPane)tbAllPages.getTabs().get(currentTab).getContent()).getContent()).SetActionMode(EActionMode.CreateLosange);
    }

    @FXML
    void ASelect(ActionEvent event) {
        if (currentTab == -1) return;
        ((RendererPage)((ScrollPane)tbAllPages.getTabs().get(currentTab).getContent()).getContent()).SetActionMode(EActionMode.Selection);
    }

    @FXML
    void ASquare(ActionEvent event) {
        if (currentTab == -1) return;
        ((RendererPage)((ScrollPane)tbAllPages.getTabs().get(currentTab).getContent()).getContent()).SetActionMode(EActionMode.CreateSquare);
    }

    @FXML
    void ARectangle(ActionEvent event) {
        if (currentTab == -1) return;
        ((RendererPage)((ScrollPane)tbAllPages.getTabs().get(currentTab).getContent()).getContent()).SetActionMode(EActionMode.CreateRectangle);
    }

    // Méthode appelée lorsqu'une forme est sélectionnée dans la page
    private void GuiShapeSeleced(AShape shape) throws IOException {
        if (pageOutliner.getSelectionModel().selectedItemProperty().get() == null || pageOutliner.getSelectionModel().selectedItemProperty().get().GetShape() != shape){
            for (int i = 0; i < pageOutliner.getItems().size(); i++){
                if (pageOutliner.getItems().get(i).GetShape() == shape){
                    pageOutliner.getSelectionModel().select(i);
                    break;
                }
            }
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/drawer_gui_shape_edit.fxml"));
        VBox root = loader.load();
        DrawerGuiShapeEditorController drawerGuiShapeEditorController = loader.getController();
        drawerGuiShapeEditorController.SetShapeToEdit(shape, pageOutliner.getSelectionModel().selectedItemProperty().get(),((RendererPage)((ScrollPane)tbAllPages.getTabs().get(currentTab).getContent()).getContent()).GetSize());
        drawerGuiShapeEditorController.SetActualizeRender(new DrawerGuiShapeEditorController.ActualizeRender() {
            @Override
            public void Actualize() {
                ((RendererPage)((ScrollPane)tbAllPages.getTabs().get(currentTab).getContent()).getContent()).Render();
            }
        });

        scrollProperties.setContent(root);
    }

    @FXML
    void OpenPages(ActionEvent event) throws IOException {
        Alert openFile = new Alert(Alert.AlertType.NONE);
        openFile.setTitle("Open drawing page");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/drawer_open.fxml"));
        VBox root = loader.load();
        DrawerOpenController drawerOpenController = loader.getController();
        openFile.getDialogPane().setContent(root);

        ButtonType btnOpen = new ButtonType("Open");
        ButtonType btnCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        openFile.getButtonTypes().setAll(btnOpen, btnCancel);

        Optional<ButtonType> choice = openFile.showAndWait();
        if (choice.get() == btnOpen && drawerOpenController.GetFileOpen() != null) {
            for (String f : currentOpenFiles){
                if (f.equals(drawerOpenController.GetFileOpen().getPath())){
                    return;
                }
            }
            CreateOrAddRendererPage(RendererPage.LoadShapesFromFile(drawerOpenController.GetFileOpen()));
        }
    }

    // Méthode pour créer ou ajouter une page de rendu
    private void CreateOrAddRendererPage(RendererPage page){
        currentOpenFiles.add(page.GetFile().getPath());
        currentRenderPage = page;

        currentRenderPage.SetShapeProperty(new IShapeProperty() {
            @Override
            public void SelectedShape(AShape shape) throws IOException {
                GuiShapeSeleced(shape);
            }
        });

        currentRenderPage.ShapeAdd(new RendererPage.AddShape() {
            @Override
            public void NewShape(AShape shape, int index) {
                pageOutliner.getItems().add(index, new ShapeOutlinerEditor(shape, currentRenderPage.GetShapeSize() - 1));
            }

            @Override
            public void RemoveShape(AShape shape, int index) {
                if (index >= 0 && index < pageOutliner.getItems().size()){
                    for (int i = index; i < pageOutliner.getItems().size(); i++){
                        pageOutliner.getItems().get(i).DecrementIndex();
                    }
                    pageOutliner.getItems().remove(index);
                }
            }
        });

        ScrollPane pageScroll = new ScrollPane();
        pageScroll.setPannable(true);
        pageScroll.setPrefViewportWidth(page.GetSize().x);
        pageScroll.setPrefViewportHeight(page.GetSize().y);
        pageScroll.setContent(page);
        pageScroll.setPadding(new Insets(10, 10, 10, 10));

        this.Center(pageScroll.getViewportBounds());
        pageScroll.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
            this.Center(newValue);
        });

        Tab tbPage = new Tab(page.GetFile().getName());
        tbPage.setClosable(true);

        tbPage.setOnCloseRequest(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                tbPage.setContent(null);
            }
        });

        tbPage.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                currentRenderPage.SetRender(!currentRenderPage.IsRender());
            }
        });

        tbPage.setContent(pageScroll);
        tbAllPages.getTabs().add(tbPage);
        tbAllPages.getSelectionModel().select(tbAllPages.getTabs().size() - 1);
        currentTab = tbAllPages.getSelectionModel().getSelectedIndex();

        idSaveCurrent.setDisable(false);
        btnExport.setDisable(false);

        DefaultActionMode(page.GetActionMode());
    }

    // Méthode pour sauvegarder la page courante
    @FXML
    void SaveCurrent(ActionEvent event) {
        try {
            Save((RendererPage)((ScrollPane)tbAllPages.getTabs().get(currentTab).getContent()).getContent(), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Méthode pour sauvegarder la page courante avec un nouveau nom
    @FXML
    void SaveCurrentAs(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export File");
        File homeDir = new File(System.getProperty("user.home"));
        fileChooser.setInitialDirectory(homeDir);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Drawer File", "*.drw"));

        File selectedFile = fileChooser.showSaveDialog(new Stage());

        if (selectedFile != null) {
            try {
                Save((RendererPage) ((ScrollPane) tbAllPages.getTabs().get(currentTab).getContent()).getContent(), selectedFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void Save(RendererPage rendererPage, File newName) throws IOException {
        File file = (newName == null) ? rendererPage.GetFile() : newName;
        rendererPage.SaveShapesToFile(file);

        if (newName != null){
            AddNewRecord(file.getAbsolutePath());
        }
    }

    // Méthode pour exporter la page courante en tant qu'image
    @FXML
    void Export(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export File");
        File homeDir = new File(System.getProperty("user.home"));
        fileChooser.setInitialDirectory(homeDir);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"));

        File selectedFile = fileChooser.showSaveDialog(new Stage());
        if (selectedFile != null) {
            try {
                ((RendererPage)((ScrollPane)tbAllPages.getTabs().get(currentTab).getContent()).getContent()).SaveAsImage(Extension(selectedFile.getName()), selectedFile);
            }
            catch (Exception e) {
                System.err.println("ERROR: Unable to open the file");
            }
        }
    }

    private String Extension(String file){
        if (file.contains(".png")) return "png";
        if (file.contains(".jpg")) return "jpg";
        if (file.contains(".jpeg")) return "jpeg";
        return "bmp";
    }

    // Méthode pour charger les fichiers récemment utilisés
    private void LoadRecentFiles() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/app/properties.drawer");
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                recentFiles.clear();

                while ((line = reader.readLine()) != null) {
                    recentFiles.add(line.trim());
                }

                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour ajouter un nouveau fichier à la liste des fichiers récemment utilisés
    public void AddNewRecord(String filePath) {
        for (String f : recentFiles){
            if (f.equals(filePath)){
                return;
            }
        }
        recentFiles.add(filePath);
        String path = getClass().getResource("/app/properties.drawer").getPath();

        try {
            FileWriter fileWriter = new FileWriter(path, true);
            PrintWriter writer = new PrintWriter(new BufferedWriter(fileWriter));
            writer.println(filePath);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Méthode pour supprimer une forme de la page courante
    @FXML
    void RemoveShape(ActionEvent event) {
        ((RendererPage)((ScrollPane)tbAllPages.getTabs().get(currentTab).getContent()).getContent()).RemoveSelected();
    }
}
