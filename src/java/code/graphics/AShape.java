/*
 * Nom du fichier: AShape.java
 * Auteur: TEUGUIA TADJUIDJE Rodolf Séderis
 * Description: Classe abstraite représentant une forme graphique avec des propriétés de transformation.
 */
package graphics;

import graphics.maths.Angle;
import graphics.maths.ITransform;
import graphics.maths.Rec2D;
import graphics.maths.Vector2D;
import graphics.shapes.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/*
* Pour une application concrete plus robuste et plus polyvalente l'utilisation d'une structure de type AMesh serait
* plus approprier en effet la manipulation de forme sera plus simple car on pourrait manipuler directement les vertices
* de chaque forme et fournir pourquoi pas des couleurs différentes des epaisseur différente etc.
* */

public abstract class AShape implements ITransform, IProperty{
    public enum Categorie{
        Undefine, Square, Rectangle, Circle, Hexagone, Losange, Ellipse
    }
    private Vector2D position = new Vector2D();
    private Vector2D size = new Vector2D(1);
    private Angle angle = new Angle();
    private Vector2D scale = new Vector2D(1.0);

    protected Rec2D globalBound = new Rec2D();
    private double strock;
    private Color strockColor = Color.GREY;
    private Color fillColor = Color.WHITE;
    private boolean hasFill = true;
    private boolean hasStrock = true;
    protected String Name = "Shape";

    public AShape(){
    }

    public AShape(Vector2D position, Vector2D size){
        this.position = position;
        this.size = size;
    }

    public Categorie GetCategorie() {
        return Categorie.Undefine;
    }

    public static String ConvertToStringSaveFile(AShape shape) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(shape.GetCategorie().toString()).append(";")
                     .append(shape.GetPosition().ToString()).append(";")
                     .append(shape.GetSize().ToString()).append(";")
                     .append(shape.GetAngle().GetDegree()).append(";")
                     .append(shape.GetScale().ToString()).append(";")
                     .append(shape.GetStrock()).append(";")
                     .append(shape.GetStrockColor().toString()).append(";")
                     .append(shape.GetFillColor().toString()).append(";")
                     .append(shape.IsHasFill()).append(";")
                     .append(shape.IsHasStrock()).append(";")
                     .append(shape.GetName());
        return stringBuilder.toString();
    }

    public static AShape ConvertFromStringSaveFile(String shapeString) {
        String[] parts = shapeString.split(";");
        if (parts.length < 11) {
            return null;
        }

        try {
            Categorie category = Categorie.valueOf(parts[0]);
            Vector2D position = Vector2D.FromString(parts[1]);
            Vector2D size = Vector2D.FromString(parts[2]);
            Angle angle = new Angle(Double.parseDouble(parts[3]));
            Vector2D scale = Vector2D.FromString(parts[4]);
            double strock = Double.parseDouble(parts[5]);
            Color strockColor = Color.web(parts[6]);
            Color fillColor = Color.web(parts[7]);
            boolean hasFill = Boolean.parseBoolean(parts[8]);
            boolean hasStrock = Boolean.parseBoolean(parts[9]);
            String name = parts[10];

            AShape shape = CreateShape(position, size, category);

            if (shape == null) return shape;

            shape.SetAngle(angle);
            shape.SetScale(scale);
            shape.SetStrock(strock);
            shape.SetStrockColor(strockColor);
            shape.SetFillColor(fillColor);
            shape.SetHasFill(hasFill);
            shape.SetHasStrock(hasStrock);
            shape.SetName(name);

            return shape;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static AShape CreateShape(Vector2D position, Vector2D size, Categorie categorie) {
        if (categorie == Categorie.Square){
            return new CarrerShape(position, size.x);
        } else if (categorie == Categorie.Rectangle){
            return new RectangleShape(position, size);
        } else if (categorie == Categorie.Ellipse){
            return new EllipseShape(position, size.Multiply(0.5));
        } else if (categorie == Categorie.Circle){
            return new CircleShape(position, size.x / 2.0);
        } else if (categorie == Categorie.Losange){
            return new LosangeShape(position, size.Multiply(0.5));
        } else if (categorie == Categorie.Hexagone){
            return new HexagoneShape(position, size.Multiply(0.5));
        }
        return null;
    }

    public abstract int GetNumberOfVertex();
    protected abstract void Draw(GraphicsContext context);

    public boolean IsHasFill() {
        return hasFill;
    }

    public void SetHasFill(boolean hasFill) {
        this.hasFill = hasFill;
    }

    public boolean IsHasStrock() {
        return hasStrock;
    }

    public void SetHasStrock(boolean hasStrock) {
        this.hasStrock = hasStrock;
    }

    public Rec2D GetGlobalBound() {
        return globalBound;
    }

    public double GetStrock() {
        return strock;
    }

    public void SetStrock(double strock) {
        this.strock = strock;
    }

    public Color GetStrockColor() {
        return strockColor;
    }

    public void SetStrockColor(Color strockColor) {
        this.strockColor = strockColor;
    }

    public Color GetFillColor() {
        return fillColor;
    }

    public void SetFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Vector2D GetPosition() {
        return position;
    }

    public void SetPosition(Vector2D position) {
        this.position = position;
    }

    public Vector2D GetSize() {
        return size;
    }

    public void SetSize(Vector2D size) {
        this.size = size;
    }

    public Angle GetAngle() {
        return new Angle(angle);
    }

    public void SetAngle(Angle angle) {
        this.angle = angle;
    }

    public Vector2D GetScale() {
        return scale;
    }

    protected void SetScale(Vector2D scale) {
        this.scale = scale;
    }

    @Override
    public void Rotate(Angle angle) {
        SetAngle(this.angle.Add(angle));
    }

    @Override
    public void Rotate(Vector2D center, Angle angle) {
        SetAngle(this.angle.Add(angle));
        SetPosition(this.position.Rotation(angle.GetDegree(), center));
    }

    @Override
    public void Translate(Vector2D delta) {
        SetPosition(position.Translation(delta));
    }

    @Override
    public void Scale(double scale) {
        this.scale = this.scale.Multiply(scale);
        this.size = this.size.Multiply(this.scale);
    }

    @Override
    public void Scale(Vector2D center, double scale) {
        this.scale = this.scale.Multiply(scale);
        this.position = this.position.Scale(scale, center);
        this.size = this.size.Multiply(this.scale);
    }

    @Override
    public void Scale(Vector2D scale) {
        this.scale = this.scale.Multiply(scale);
        this.size = this.size.Multiply(this.scale);
    }

    @Override
    public void Scale(Vector2D center, Vector2D scale) {
        this.scale = this.scale.Multiply(scale);
        this.position = this.position.Scale(scale, center);
        this.size = this.size.Multiply(this.scale);
    }

    protected void ApplyTransform(GraphicsContext context){
        context.translate(position.x, position.y);
        context.scale(scale.x, scale.y);
        context.rotate(angle.GetDegree());
    }

    public void Render(GraphicsContext context){
        context.save();

        ApplyTransform(context);
        Draw(context);

        context.restore();
    }

    public boolean Contains(Vector2D vector2D) {
        if (vector2D.x < position.x - size.x / 2 || vector2D.x > position.x + size.x/2 ||
                vector2D.y < position.y - size.y / 2 || vector2D.y > position.y + size.y/2){
            return false;
        }
        return true;
    }

    public boolean IsEnable() {
        return IsHasFill() || IsHasStrock();
    }

    public String GetName() {
        return Name;
    }

    public void SetName(String name) {
        Name = name;
    }
}
