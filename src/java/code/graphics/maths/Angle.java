/*
 * Nom du fichier: Angle.java
 * Auteur: TEUGUIA TADJUIDJE Rodolf Séderis
 * Description: Classe représentant un angle en degrés, avec des méthodes pour effectuer des opérations
 *              et des conversions entre degrés et radians.
 */
package graphics.maths;

public class Angle {
    // Valeur de l'angle en degrés
    private double degree = 0.0;

    // Constructeur par défaut
    public Angle() {
    }

    // Constructeur avec une valeur d'angle spécifiée en degrés
    public Angle(double degree) {
        this.degree = degree;
    }

    // Constructeur avec un objet Angle pour la copie
    public Angle(Angle angle) {
        degree = angle.degree;
    }

    // Méthode pour obtenir la valeur de l'angle en degrés
    public double GetDegree(){
        return degree;
    }

    // Méthode pour obtenir la valeur de l'angle en radians
    public double GetRadian(){
        return Math.toRadians(degree);
    }

    // Méthode statique pour convertir des degrés en radians
    public static double ToRadians(double degrees) {
        return Math.toRadians(degrees);
    }

    // Méthode statique pour convertir des radians en degrés
    public static double ToDegrees(double radians) {
        return Math.toDegrees(radians);
    }

    // Méthode statique pour créer un objet Angle à partir d'une valeur en degrés
    public static Angle FromDegree(double degree){
        return new Angle(degree);
    }

    // Méthode statique pour créer un objet Angle à partir d'une valeur en radians
    public static Angle FromRadian(double radian){
        return new Angle(Math.toDegrees(radian));
    }

    // Méthode pour additionner deux angles
    public Angle Add(Angle angle) {
        return new Angle(degree + angle.degree);
    }

    // Méthode pour soustraire un angle d'un autre
    public Angle Subtract(Angle angle) {
        return new Angle(degree - angle.degree);
    }
}
