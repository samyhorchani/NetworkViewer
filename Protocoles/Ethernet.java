/* ---------------------------------------------------------------------------------------------------------------------
       Package
----------------------------------------------------------------------------------------------------------------------*/

package Protocoles;

/* ---------------------------------------------------------------------------------------------------------------------
       Importations
----------------------------------------------------------------------------------------------------------------------*/

import java.util.List;

/* ---------------------------------------------------------------------------------------------------------------------
       Protocole Ethernet
----------------------------------------------------------------------------------------------------------------------*/

public class Ethernet implements Protocole {

    /* -----------------------------------------------------------------------------------------------------------------
        Attributs
    ------------------------------------------------------------------------------------------------------------------*/

    private List<String> trame; // Trame Ethernet II
    private StringBuilder dst = new StringBuilder(); // Adresse materielle de l'adapteur reseau cible (6 octets)
    private StringBuilder src = new StringBuilder(); // Adresse materielle de l'adapteur reseau source (6 octets)
    private StringBuilder type = new StringBuilder(); // Marquage des protocoles de la couche 3 (2 octets)
    private IP IP; // Paquet IP

    /* -----------------------------------------------------------------------------------------------------------------
        Constructeurs
    ------------------------------------------------------------------------------------------------------------------*/

    public Ethernet(List<String> trame) {
        this.trame = trame;

        for (int i = 0; i < 6; i++) {
            dst.append(trame.get(i));
            if (i != 5) { dst.append(":"); }
        }
        for (int i = 6; i < 12; i++) {
            src.append(trame.get(i));
            if (i != 11) { src.append(":"); }
        }
        for (int i = 12; i < 14; i++) { type.append(trame.get(i)); }
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Méthodes et assesseurs
    ------------------------------------------------------------------------------------------------------------------*/

    public IP analyseType() {
        if (type.toString().equals("0800")) { IP = new IP(trame.subList(14, trame.size())); }
        else { IP = null; }
        return IP;
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("\nEthernet II, Src : ").append(src).append(", Dst : ").append(dst);
        sb.append("\n\tDestination : ").append(dst).append("\n\tSource : ").append(src);
        sb.append("\n\tType : 0x").append(type);

        return sb.toString();
    }

    public String getMacSrc(){
        return src.toString();
    }

    public String getMacDst(){
        return dst.toString();
    }

    public String getType(){
        return type.toString();
    }
}