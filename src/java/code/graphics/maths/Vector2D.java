/*
 * Nom du fichier: Vector2D.java
 * Auteur: TEUGUIA TADJUIDJE Rodolf Séderis
 * Description: Classe représentant un vecteur 2D dans un espace mathématique.
 */

package graphics.maths;

public class Vector2D {
    // Coordonnées x et y du vecteur
    public double x;
    public double y;

    // Constructeur par défaut initialisant un vecteur à l'origine
    public Vector2D() {
        x = 0;
        y = 0;
    }

    // Constructeur initialisant le vecteur avec une valeur donnée
    public Vector2D(double value) {
        x = y = value;
    }

    // Constructeur initialisant le vecteur avec des coordonnées spécifiques
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Constructeur copie
    public Vector2D(Vector2D v) {
        x = v.x;
        y = v.y;
    }

    // Méthode pour effectuer une rotation du vecteur autour d'un point d'origine avec un angle donné
    public Vector2D Rotation(double angle, Vector2D origin) {
        if (origin == null) {
            return null;
        }

        double cosA = Math.cos(Angle.ToRadians(angle));
        double sinA = Math.sin(Angle.ToRadians(angle));

        return Rotation(cosA, sinA, origin);
    }

    // Méthode pour effectuer une rotation du vecteur autour d'un point d'origine avec des coefficients cosA et sinA donnés
    public Vector2D Rotation(double cosA, double sinA, Vector2D origin) {
        if (origin == null) {
            return null;
        }

        // Translater le vecteur par rapport à l'origine
        double translatedX = x - origin.x;
        double translatedY = y - origin.y;

        // Appliquer la rotation
        double rotatedX = translatedX * cosA - translatedY * sinA;
        double rotatedY = translatedX * sinA + translatedY * cosA;

        return new Vector2D(rotatedX + origin.x, rotatedY + origin.y);
    }

    // Méthode pour effectuer une translation du vecteur
    public Vector2D Translation(Vector2D delta) {
        return new Vector2D(x + delta.x, y + delta.y);
    }

    // Méthode pour effectuer une mise à l'échelle du vecteur par rapport à un point d'origine
    public Vector2D Scale(double scale, Vector2D origin) {
        return Scale(scale, scale, origin);
    }

    // Méthode pour effectuer une mise à l'échelle du vecteur par rapport à un point d'origine avec des facteurs de mise à l'échelle différents
    public Vector2D Scale(double scaleX, double scaleY, Vector2D origin) {
        return new Vector2D(origin.x + scaleX * (x - origin.x), origin.y + scaleY * (y - origin.y));
    }

    // Méthode pour effectuer une mise à l'échelle du vecteur par rapport à un point d'origine avec un vecteur d'échelle
    public Vector2D Scale(Vector2D scale, Vector2D origin) {
        return new Vector2D(origin.x + scale.x * (x - origin.x), origin.y + scale.y * (y - origin.y));
    }

    // Méthode pour calculer la norme du vecteur
    public double Norm() {
        return Math.sqrt(x * x + y * y);
    }

    // Méthode pour calculer la distance entre deux vecteurs
    public double Distance(Vector2D other) {
        if (other == null) {
            throw new IllegalArgumentException("Argument 'other' cannot be null.");
        }
        return other.Subtract(this).Norm();
    }

    // Méthode pour calculer l'angle entre deux vecteurs
    public double Angle(Vector2D other) {
        if (other == null) {
            throw new IllegalArgumentException("Argument 'other' cannot be null.");
        }

        double dotProduct = this.Dot(other);
        double normProduct = this.Norm() * other.Norm();

        return Math.acos(dotProduct / normProduct);
    }

    // Méthode pour calculer le produit scalaire avec un autre vecteur
    public double Dot(Vector2D other) {
        if (other == null) {
            throw new IllegalArgumentException("Argument 'other' cannot be null.");
        }

        return this.x * other.x + this.y * other.y;
    }

    // Méthode pour additionner deux vecteurs
    public Vector2D Add(Vector2D other) {
        if (other == null) {
            throw new IllegalArgumentException("Argument 'other' cannot be null.");
        }

        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    // Méthode pour soustraire deux vecteurs
    public Vector2D Subtract(Vector2D other) {
        if (other == null) {
            throw new IllegalArgumentException("Argument 'other' cannot be null.");
        }

        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    // Méthode pour multiplier le vecteur par un scalaire
    public Vector2D Multiply(double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    // Méthode pour multiplier le vecteur par des scalaires différents pour chaque composante
    public Vector2D Multiply(double scalarX, double scalarY) {
        return new Vector2D(this.x * scalarX, this.y * scalarY);
    }

    // Méthode pour multiplier le vecteur par un autre vecteur (composante par composante)
    public Vector2D Multiply(Vector2D scale) {
        return new Vector2D(this.x * scale.x, this.y * scale.y);
    }

    // Méthode pour inverser le vecteur
    public Vector2D Invert() {
        return new Vector2D(-this.x, -this.y);
    }

    // Méthode pour obtenir une représentation sous forme de chaîne de caractères
    public String ToString() {
        return x + "," + y;
    }

    // Méthode pour créer un vecteur à partir d'une chaîne de caractères
    public static Vector2D FromString(String part) {
        String[] vl = part.split(",");
        if (vl.length != 2) {
            System.out.println("len = " + vl.length + ", " + part);
            return null;
        }
        Vector2D result = new Vector2D(Double.parseDouble(vl[0]), Double.parseDouble(vl[1]));
        if (result == null) {
            System.out.println("in = " + vl[0] + ", " + vl[1]);
        }
        return result;
    }
}
