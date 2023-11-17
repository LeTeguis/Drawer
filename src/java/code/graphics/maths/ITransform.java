/*
 * Nom du fichier: ITransform.java
 * Auteur: TEUGUIA TADJUIDJE Rodolf Séderis
 * Description: Interface définissant des méthodes pour effectuer des transformations géométriques telles que la rotation,
 *              la translation et la mise à l'échelle.
 */

package graphics.maths;

public interface ITransform {
    // Méthode pour effectuer une rotation autour de l'origine
    void Rotate(Angle angle);

    // Méthode pour effectuer une rotation autour d'un point spécifié
    void Rotate(Vector2D center, Angle angle);

    // Méthode pour effectuer une translation
    void Translate(Vector2D delta);

    // Méthodes pour effectuer une mise à l'échelle
    void Scale(double scale);
    void Scale(Vector2D center, double scale);
    void Scale(Vector2D scale);
    void Scale(Vector2D center, Vector2D scale);
}
