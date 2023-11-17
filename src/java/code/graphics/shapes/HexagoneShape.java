package graphics.shapes;

import graphics.AShape;
import graphics.maths.Vector2D;
import javafx.scene.canvas.GraphicsContext;

public class HexagoneShape extends AShape {
    public HexagoneShape(){
        super();
        Name = "Hexagone";
    }

    public HexagoneShape(Vector2D position, Vector2D size){
        super(position, size.Multiply(2));
        Name = "Hexagone";
    }

    @Override
    public Categorie GetCategorie() {
        // À implémenter en fonction de la catégorie spécifique de la forme (rectangle, cercle, etc.)
        return Categorie.Hexagone;
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

        double centerX = 0; // x-coordinate du centre de l'hexagone
        double centerY = 0; // y-coordinate du centre de l'hexagone
        double radiusX = GetSize().x / 2; // Rayon en x
        double radiusY = GetSize().y / 2; // Rayon en y

        context.beginPath();
        for (int i = 0; i < 6; i++) {
            double angleRad = Math.toRadians(60 * i);
            double x = centerX + radiusX * Math.cos(angleRad);
            double y = centerY + radiusY * Math.sin(angleRad);

            if (i == 0) {
                context.moveTo(x, y);
            } else {
                context.lineTo(x, y);
            }
        }
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
