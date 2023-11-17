package graphics.shapes;

import graphics.AShape;
import graphics.maths.Vector2D;
import javafx.scene.canvas.GraphicsContext;

public class EllipseShape extends AShape {
    public EllipseShape(){
        super();
        Name = "Ellipse";
    }

    public EllipseShape(Vector2D position, Vector2D size){
        super(position, size.Multiply(2));
        Name = "Ellipse";
    }

    @Override
    public Categorie GetCategorie() {
        // À implémenter en fonction de la catégorie spécifique de la forme (rectangle, cercle, etc.)
        return Categorie.Ellipse;
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
            context.strokeOval(-GetSize().x / 2, -GetSize().y / 2, GetSize().x, GetSize().y);
        }
        if (IsHasFill()){
            context.strokeOval(-GetSize().x / 2, -GetSize().y / 2, GetSize().x, GetSize().y);
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
