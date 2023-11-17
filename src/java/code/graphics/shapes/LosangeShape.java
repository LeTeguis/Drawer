package graphics.shapes;

import graphics.AShape;
import graphics.maths.Vector2D;
import javafx.scene.canvas.GraphicsContext;

public class LosangeShape extends AShape {
    public LosangeShape(){
        super();
        Name = "Losange";
    }

    public LosangeShape(Vector2D position, Vector2D size){
        super(position, size.Multiply(2));
        Name = "Losange";
    }

    @Override
    public Categorie GetCategorie() {
        // À implémenter en fonction de la catégorie spécifique de la forme (rectangle, cercle, etc.)
        return Categorie.Losange;
    }

    @Override
    public int GetNumberOfVertex() {
        return 4;
    }

    @Override
    protected void Draw(GraphicsContext context) {
        context.setLineWidth(GetStrock());
        context.setStroke(GetStrockColor());
        context.setFill(GetFillColor());

        context.beginPath();
        context.moveTo(-GetSize().x / 2, 0);
        context.lineTo(0, -GetSize().y / 2);
        context.lineTo(GetSize().x / 2, 0);
        context.lineTo(0, GetSize().y / 2);
        context.closePath();

        if (IsHasStrock()){
            context.stroke();
        }
        if (IsHasFill()){
            context.fill();
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
