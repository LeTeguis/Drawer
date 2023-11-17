# Drawer

Drawer est un petit projet de dessin utilisant des formes prédéfinies telles que des carrés, des rectangles, des losanges, etc.

## Outils et Technologie Utilisés

- IDE: IntelliJ IDEA 2022
- Langage: Java
- Bibliothèque: JavaFX 19

L'installation des outils nécessite le JDK de Java, un IDE, et l'installation de JavaFX 19.

## Description du Projet

Le projet se comporte comme une application de dessin sur PC qui ne nécessite pas de base de données et utilise à la
place des fichiers tout simplement. Après avoir lancé le projet, vous pouvez soit créer un nouveau projet, soit ouvrir
un projet existant.

En plus de cela, plusieurs options d'exportation sont disponibles, par exemple, la sauvegarde de la page en PNG
(les autres extensions ne fonctionnent pas encore correctement). En raison de contraintes de temps, l'option de
suppression d'une forme dans une page n'est pas totalement fonctionnelle et contient quelques bogues pour l'instant.

L'ajout d'une forme est visualisé à la fois dans la page principale de dessin et dans l'outliner. Vous pouvez changer
la couleur de fond de la page en cours et manipuler une forme depuis ses propriétés.

L'ajout d'une forme se fait en cliquant-déplaçant-cliquant. Une fois l'option sélectionnée ajoutée, vous ne pouvez plus
 ajouter de formes, vous pouvez seulement les manipuler. La sélection se fait actuellement par double-clic, et le
 déplacement de la forme se fait par cliquer-déplacer-cliquer.


NB : Lord de l'installation de la bibliotheque javafx n'oubliez pas d'ajouter (VM Option) et le mien est le suivant:
--module-path "path\to\javafx-sdk-19\lib" --add-modules javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.web

remplacer path\to\javafx-sdk-19\lib par votre chemin ou ce trouve le sdk javafx-19
