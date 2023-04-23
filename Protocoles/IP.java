/* ---------------------------------------------------------------------------------------------------------------------
       Package
----------------------------------------------------------------------------------------------------------------------*/

package Protocoles;

/* ---------------------------------------------------------------------------------------------------------------------
       Importations
----------------------------------------------------------------------------------------------------------------------*/

import java.util.List;
import Outils.*;

/* ---------------------------------------------------------------------------------------------------------------------
       Protocole IP
----------------------------------------------------------------------------------------------------------------------*/

public class IP implements Protocole {

    /* -----------------------------------------------------------------------------------------------------------------
        Attributs
    ------------------------------------------------------------------------------------------------------------------*/

    private List<String> paquet; // Paquet IP

    private String version; // Version du protocole IP (0,5 octets)
    private long lngEntete; // Taille de l'entête (0,5 octets)
    private String TOS; // Type de chemin sur lequel acheminer le paquet (1 octet)

    private long lngTt; // Taille totale du paquet (2 octets)
    private StringBuilder ID = new StringBuilder(); // Identifiant (2 octets)
    private String RB; // Reserved bit
    private String DF; // Don't fragment
    private String MF; // More fragment
    private String FO; // Fragment offset
    private long TTL; // Limite de la duree de vie des paquets captures (1 octet)

    private String protocole; // Type de l'entete situe apres l'entete IP (1 octet)
    private StringBuilder checksum = new StringBuilder(); // Code de detection d'erreurs (2 octets)

    private StringBuilder src = new StringBuilder(); // Adresse IP de la source (4 octets)
    private StringBuilder dst = new StringBuilder(); // Adresse IP de la cible (4 octets)

    private StringBuilder optionIP = new StringBuilder();

    private TCP TCP; // Segment TCP

    /* -----------------------------------------------------------------------------------------------------------------
        Constructeurs
    ------------------------------------------------------------------------------------------------------------------*/

    public IP(List<String> paquet) {
        this.paquet = paquet;

        version = paquet.get(0).substring(0, 1);
        lngEntete = Tools.getDecValues(paquet.get(0).substring(1, 2))*4;
        TOS = paquet.get(1);

        for (int i = 2; i < 4; i++) { lngTt = lngTt + (Tools.getDecValues(paquet.get(i))); }

        for (int i = 4; i < 6; i++) { ID.append(paquet.get(i)); }
        String value = paquet.get(6);
        value += paquet.get(7);
        this.FO = new String(value);
        String premierChar = value.charAt(0) + "";
        premierChar = Tools.hexToBin(premierChar);
        this.RB = premierChar.charAt(0) + "";
        this.DF = premierChar.charAt(1) + "";
        this.MF = premierChar.charAt(2) + "";

        value = value.substring(1, 3);
        value = premierChar.charAt(3) + value;
        FO = Integer.parseInt(value, 16) + "";

        /*RB = paquet.get(6).substring(0, 1);
        DF = paquet.get(6).substring(1, 2);
        MF = paquet.get(7).substring(0, 1);
        FO = paquet.get(7).substring(1, 2);
        */

        TTL = Tools.getDecValues(paquet.get(8));

        protocole = paquet.get(9);

        for (int i = 10; i < 12; i++) { checksum.append(paquet.get(i)); }

        for (int i = 12; i < 16; i++) {
            src.append(Tools.getDecValues(paquet.get(i)));
            if (i != 15) { src.append("."); }
        }

        for (int i = 16; i < 20; i++) {
            dst.append(Tools.getDecValues(paquet.get(i)));
            if (i != 19) { dst.append("."); }
        }

        if(hasOption()){
            for(int i = 20; i < (int)(lngEntete) ; i++ ){
                optionIP.append( paquet.get(i));
            }
        }
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Méthodes et assesseurs
    ------------------------------------------------------------------------------------------------------------------*/

    public TCP analyseProtocole() {
        if (protocole.equals("06")) { TCP = new TCP(paquet.subList((int)lngEntete, paquet.size())); }
        else { TCP = null; }
        return TCP;
    }

    public boolean hasOption() {
        if(lngEntete > 20){
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\nInternet Protocol, Src : ").append(src).append(", Dst : ").append(dst);

        sb.append("\n\tVersion : ");
        if (version.equals("4")) { sb.append(" IPV4 (0x").append(version).append(")"); }
        if (version.equals("6")) { sb.append(" IPV6 (0x").append(version).append(")"); }

        sb.append("\n\tHeader length : ");
        if (lngEntete <= 20) { sb.append(lngEntete).append(" bytes (no options)"); }
        if (lngEntete > 20) { sb.append("20 bytes (options length = ").append(lngEntete - 20).append(" bytes)"); }

        sb.append("\n\tDifferentiated services field : 0x").append(TOS);

        sb.append("\n\tTotal length : ").append(lngTt);

        sb.append("\n\tIdentification : 0x").append(ID);
        sb.append(" (").append(Tools.getDecValues(ID.toString())).append(")");

        sb.append("\n\tFlags : ");
        sb.append("\n\t\t").append(RB).append(".... = Reserved bits : " + Tools.isSet(RB));
        sb.append("\n\t\t").append(".").append(DF).append(".. = Don't fragment : " + Tools.isSet(DF));
        sb.append("\n\t\t").append("..").append(MF).append(". = More fragments : " + Tools.isSet(MF));

        sb.append("\n\tFragment offset : ").append(FO);

        sb.append("\n\tTime to live : ").append(TTL);

        sb.append("\n\tProtocol : ");
        if (protocole.equals("01")) { sb.append("ICMP "); }
        if (protocole.equals("06")) { sb.append("TCP "); }
        if (protocole.equals("17")) { sb.append("UDP "); }
        sb.append("(0x").append(protocole).append(")");

        sb.append("\n\tHeader checksum : 0x").append(checksum);

        sb.append("\n\tSource : ").append(src);
        sb.append("\n\tDestination : ").append(dst);
        if(hasOption()){
            sb.append("\n\t Options (raw) : " + optionIP);
        }else{
            sb.append("\n\tCette trame ne contient pas d'option IP");
        }
        return sb.toString();
    }

    public TCP getTcp(){
        return TCP;
    }

    public String getIpSrc(){
        return src.toString();
    }


    public String getIpDst(){
        return dst.toString();
    }

    public String getProtocol(){
        return protocole;
    }
}
