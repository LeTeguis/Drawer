package graphics.shapes;

import graphics.AShape;
import graphics.maths.Vector2D;
import javafx.scene.canvas.GraphicsContext;

public class CircleShape extends AShape {
    public CircleShape(){
        super();
        Name = "Circle";
    }

    public CircleShape(Vector2D position, double rayon){
        super(position, new Vector2D(rayon * 2, rayon * 2));
        Name = "Circle";
    }

    @Override
    public Categorie GetCategorie() {
        // À implémenter en fonction de la catégorie spécifique de la forme (rectangle, cercle, etc.)
        return Categorie.Circle;
    }

    @Override
    public int GetNumberOfVertex() {
        return 4;
    }

    @Override
    protected void Draw(GraphicsContext context) {
        context.setStroke(GetStrockColor());
        context.setLineWidth(GetStrock());
        context.setFill(GetFillColor());

        if (IsHasStrock()){
            context.strokeOval(-GetSize().x / 2, -GetSize().x / 2, GetSize().x, GetSize().x);
        }
        if (IsHasFill()){
            context.strokeOval(-GetSize().x / 2, -GetSize().x / 2, GetSize().x, GetSize().x);
        }
    }

    @Override
    public double CalculerAire() {
        return GetSize().x * GetSize().x;
    }

    @Override
    public double CalculerPerimetre() {
        return GetSize().x * 4;
    }

    @Override
    public double CalculerRayonCercleEnglobant() {
        return Math.sqrt(2) * GetSize().x / 2.0;
    }

    @Override
    public double CalculerRayonCercleEnglober() {
        return GetSize().x / 2.0;
    }
}
