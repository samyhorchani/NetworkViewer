/* ---------------------------------------------------------------------------------------------------------------------
       Package
----------------------------------------------------------------------------------------------------------------------*/

package Outils;

/* ---------------------------------------------------------------------------------------------------------------------
    Ensemble des méthodes permettant d'observer les erreurs d'écriture dans un fichier
----------------------------------------------------------------------------------------------------------------------*/

public class Error implements Outil {
    private int numLigne;
    private String typeErreur;

    public Error(int numLigne, String typeErreur) {
        this.numLigne = numLigne;
        this.typeErreur = typeErreur;
    }

    public String toString() {
        return "Erreur détecté {" + "à la Ligne = " + numLigne + ", de type = '" + typeErreur + '\'' +'}';
    }
}
