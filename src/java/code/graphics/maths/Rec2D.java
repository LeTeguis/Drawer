/*
 * Nom du fichier: Rec2D.java
 * Auteur: TEUGUIA TADJUIDJE Rodolf Séderis
 * Description: Classe représentant un rectangle 2D défini par sa position (x, y) et ses dimensions (largeur et hauteur).
 */

package graphics.maths;

public class Rec2D {
    // Coordonnées x et y du coin supérieur gauche du rectangle
    double x, y;

    // Largeur et hauteur du rectangle
    double width, height;

    // Constructeur par défaut initialisant un rectangle unitaire à l'origine
    public Rec2D() {
        x = y = 0;
        width = height = 1;
    }

    // Constructeur copie
    public Rec2D(Rec2D rec2D) {
        x = rec2D.x;
        y = rec2D.y;
        height = rec2D.height;
        width = rec2D.width;
    }

    // Constructeur prenant les coordonnées x, y, la largeur et la hauteur
    public Rec2D(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }
}
