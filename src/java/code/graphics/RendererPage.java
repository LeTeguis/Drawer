package graphics;

import graphics.maths.Vector2D;
import graphics.shapes.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
* Nous eviton de traiter le temps réel ici vu que l'application n'est pas une application en temps réel
* */

public class RendererPage extends Pane {
    private List<AShape> aShapes = new ArrayList<AShape>();
    private AShape currentShape = null;
    private AShape currentShapeCreate = null;
    private Color currentShapeColor = null;
    private Vector2D mouseClick;

    private Vector2D size;
    private File file;
    private Color backgroundColor = Color.WHITE;
    private String folderName;
    private GraphicsContext context;
    private Canvas canvas;
    private boolean isRender;
    private EActionMode actionMode = EActionMode.Selection;

    private IShapeProperty shapeProperty;
    private AddShape addShape;

    public RendererPage(File file, String folderName, Vector2D size){
        this.size = size;
        this.file = file;
        this.folderName = folderName;
        setPrefHeight(size.y);
        setPrefWidth(size.x);
        setMinSize(size.x, size.y);
        setMaxSize(size.x, size.y);

        canvas = new Canvas(size.x, size.y);
        context = canvas.getGraphicsContext2D();

        getChildren().add(canvas);

        setOnMouseClicked(event -> {
            if (mouseClick == null){
                if (actionMode == EActionMode.Selection){
                    boolean selected = false;

                    for (int i = aShapes.size() - 1; i >= 0; i--) {
                        if (aShapes.get(i).Contains(new Vector2D(event.getX(), event.getY())) && aShapes.get(i).IsEnable()) {
                            if (currentShape != null && currentShapeColor != null){
                                currentShape.SetStrockColor(currentShapeColor);
                            }

                            mouseClick = new Vector2D(event.getX(), event.getY());

                            currentShapeCreate = currentShape = aShapes.get(i);
                            currentShapeColor = currentShape.GetStrockColor();

                            currentShape.SetStrockColor(new Color(255 / 255.0, 201.0 / 255.0, 14.0 / 255.0, 1.0));

                            if (shapeProperty != null){
                                try {
                                    shapeProperty.SelectedShape(currentShape);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            selected = true;
                            Render();
                            break;
                        }
                    }

                    if (!selected){
                        if (currentShape != null) {
                            currentShape.SetStrockColor(currentShapeColor);
                            currentShape = null;
                            currentShapeColor = null;
                            mouseClick = null;

                            if (shapeProperty != null){
                                try {
                                    shapeProperty.SelectedShape(null);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            Render();
                        }
                    }
                } else {
                    mouseClick = new Vector2D(event.getX(), event.getY());

                    aShapes.add(CreateShape(new Vector2D(event.getX(), event.getY()), actionMode));
                    currentShape = aShapes.get(aShapes.size() - 1);
                    currentShapeColor = currentShape.GetStrockColor();
                    currentShape.SetStrockColor(new Color(255 / 255.0, 201.0 / 255.0, 14.0 / 255.0, 1.0));

                    if (addShape != null){
                        addShape.NewShape(aShapes.get(aShapes.size() - 1), aShapes.size() - 1);
                    }

                    Render();
                }
            } else {
                mouseClick = null;
                currentShapeCreate = null;
            }
        });

        setOnMouseMoved(event -> {
            if (actionMode == EActionMode.Selection){
                if (currentShapeCreate != null){
                    currentShapeCreate.SetPosition(new Vector2D(event.getX(), event.getY()));

                    if (shapeProperty != null){
                        try {
                            shapeProperty.SelectedShape(currentShapeCreate);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    Render();
                }
            } else {
                if (currentShape != null && mouseClick != null) {
                    Vector2D result = mouseClick.Subtract(new Vector2D(event.getX(), event.getY()));
                    currentShape.SetSize(new Vector2D(Math.abs(result.x) * 2, Math.abs(result.y) * 2));

                    if (shapeProperty != null){
                        try {
                            shapeProperty.SelectedShape(currentShape);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    Render();
                }
            }
        });

        Render();
    }

    public void ShapeAdd(AddShape addShape){
        this.addShape = addShape;
    }

    public void Selected(int index){
        if (index >= 0 && index < aShapes.size()){
            if (actionMode == EActionMode.Selection) {
                if (currentShape != null && currentShapeColor != null) {
                    currentShape.SetStrockColor(currentShapeColor);
                }

                mouseClick = new Vector2D();
                currentShape = aShapes.get(index);
                currentShapeColor = currentShape.GetStrockColor();

                currentShape.SetStrockColor(new Color(255 / 255.0, 201.0 / 255.0, 14.0 / 255.0, 1.0));

                if (shapeProperty != null) {
                    try {
                        shapeProperty.SelectedShape(currentShape);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                Render();
            }
        }
    }

    public void SetShapeProperty(IShapeProperty shapeP){
        shapeProperty = shapeP;
    }

    private AShape CreateShape(Vector2D position, EActionMode actionMode) {
        if (actionMode == EActionMode.CreateSquare){
            return new CarrerShape(position, 1);
        } else if (actionMode == EActionMode.CreateRectangle){
            return new RectangleShape(position, new Vector2D(1, 1));
        } else if (actionMode == EActionMode.CreateEllipse){
            return new EllipseShape(position, new Vector2D(1, 1));
        } else if (actionMode == EActionMode.CreateCircle){
            return new CircleShape(position, 1);
        } else if (actionMode == EActionMode.CreateLosange){
            return new LosangeShape(position, new Vector2D(1, 1));
        } else if (actionMode == EActionMode.CreateHexagone){
            return new HexagoneShape(position, new Vector2D(1, 1));
        }
        return null;
    }

    public void Render() {
        context.clearRect(0, 0, size.x, size.y);

        // Dessiner le fond blanc
        context.setFill(backgroundColor);
        context.fillRect(0, 0, size.x, size.y);

        // Dessiner la bordure grise
        context.setStroke(Color.GRAY);
        context.setLineWidth(2); // Épaisseur de la bordure
        context.strokeRect(0, 0, size.x, size.y);

        for (int i = 0; i < aShapes.size(); i++){
            if (aShapes.get(i) != null) {
                aShapes.get(i).Render(context);
            }
        }
    }

    public void SetRender(boolean render){
        isRender = render;
    }

    public boolean IsRender() {
        return isRender;
    }

    public void SetActionMode(EActionMode actionMode){
        this.actionMode = actionMode;
    }

    public Vector2D GetSize(){
        return size;
    }

    public void SetSize(Vector2D size){
        this.size = size;

        setPrefHeight(size.y);
        setPrefWidth(size.x);
        setMinSize(size.x, size.y);
        setMaxSize(size.x, size.y);

        canvas = new Canvas(size.x, size.y);
        context = canvas.getGraphicsContext2D();
    }

    public int GetShapeSize(){
        return aShapes.size();
    }

    public AShape GetShape(int index){
        if (index >= 0 && index < GetShapeSize()){
            return aShapes.get(index);
        }
        return null;
    }

    public void SaveAsImage(String ext, File file) throws IOException {
        WritableImage writableImage = new WritableImage((int) size.x, (int) size.y);
        canvas.snapshot(null, writableImage);

        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
        ImageIO.write(renderedImage, ext, file);
    }

    public File GetFile() {
        return file;
    }

    public void SetBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        Render();
    }

    public Color GetBackgroundColor() {
        return backgroundColor;
    }

    public void RemoveSelected() {
        if (currentShape != null) {
            if (addShape != null){
                addShape.RemoveShape(currentShape, aShapes.indexOf(currentShape));
            }
            aShapes.remove(currentShape);
            currentShape = null;
            currentShapeColor = null;
            mouseClick = null;

            if (shapeProperty != null){
                try {
                    shapeProperty.SelectedShape(null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            Render();
        }
    }

    public EActionMode GetActionMode() {
        return actionMode;
    }

    public interface AddShape{
        void NewShape(AShape shape, int index);
        void RemoveShape(AShape shape, int index);
    }

    public static RendererPage LoadShapesFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            RendererPage rendererPage = null;
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                if (i <= 2){                    if (i == 0) {
                        Vector2D size = Vector2D.FromString(line);
                        rendererPage = new RendererPage(file, file.getParent(), size);
                    }
                    if (i == 1) {
                        rendererPage.backgroundColor = Color.valueOf(line);
                    }
                    if (i == 2) {
                        rendererPage.actionMode = EActionMode.valueOf(line);
                    }
                } else {
                    AShape shape = AShape.ConvertFromStringSaveFile(line);
                    if (shape != null) {
                        rendererPage.aShapes.add(shape);
                    }
                }
                i++;
            }
            rendererPage.Render();
            return rendererPage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean SaveShapesToFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(size.ToString());
            writer.newLine();
            writer.write(backgroundColor.toString());
            writer.newLine();
            writer.write(actionMode.toString());
            writer.newLine();
            for (AShape shape : aShapes) {
                if (shape == null) continue;

                String shapeString = AShape.ConvertToStringSaveFile(shape);
                writer.write(shapeString);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
