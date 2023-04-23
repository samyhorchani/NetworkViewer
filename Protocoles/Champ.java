/* ---------------------------------------------------------------------------------------------------------------------
       Package
----------------------------------------------------------------------------------------------------------------------*/

package Protocoles;

/* ---------------------------------------------------------------------------------------------------------------------
       Champs contenus dans le protocole HTTP
----------------------------------------------------------------------------------------------------------------------*/

public class Champ {

    /* -----------------------------------------------------------------------------------------------------------------
        Attributs
    ------------------------------------------------------------------------------------------------------------------*/

    private String nom;
    private String valeur;

    /* -----------------------------------------------------------------------------------------------------------------
        Constructeurs
    ------------------------------------------------------------------------------------------------------------------*/

    public Champ(String nom, String valeur){
        this.nom = nom;
        this.valeur = valeur;
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Méthodes et assesseurs
    ------------------------------------------------------------------------------------------------------------------*/

    public String getNom(){
        return nom;
    }

    public String getValeur(){
        return valeur;
    }

    public String toString() {
        return nom + " : " + valeur;
    }
}