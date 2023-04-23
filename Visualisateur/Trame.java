/* ---------------------------------------------------------------------------------------------------------------------
       Package
----------------------------------------------------------------------------------------------------------------------*/

package Visualisateur;

/* ---------------------------------------------------------------------------------------------------------------------
       Importations
----------------------------------------------------------------------------------------------------------------------*/

import java.util.List;

import Outils.Tools;
import Protocoles.*;

/* ---------------------------------------------------------------------------------------------------------------------
       Trame du trafic
----------------------------------------------------------------------------------------------------------------------*/

public class Trame {

    /* -----------------------------------------------------------------------------------------------------------------
        Attributs
    ------------------------------------------------------------------------------------------------------------------*/

    private int num; // Numéro de la trame
    private int cle; // Clé de la trame dans le dictionnaire

    /* Attribut(s) pour analyser la trame */
    private List<String> trame; // Trame à analyser
    private Ethernet ethernet; 
    private IP IP; 
    private TCP TCP; 
    private Datagramme data;
    private String srcIP;
    private String dstIP;
    private String srcPort;
    private String dstPort;
    private String protocol;

    /* -----------------------------------------------------------------------------------------------------------------
        Constructeurs
    ------------------------------------------------------------------------------------------------------------------*/

    public Trame(int num, int cle, List<String> trame) {
        this.num = num; this.cle = cle; this.trame = trame;
        if (trame != null && trame.size() >= 14) { 
            ethernet = new Ethernet(this.trame);
            this.protocol = "Ethernet";
            if (ethernet != null && trame.size() >= 34) { 
                IP = ethernet.analyseType();
                if(!ethernet.getType().equals("0800")){
                    this.protocol = "Unsupported";
                }
                if(IP != null){
                    if(ethernet.getType().equals("0800")){
                        this.protocol = "IPv4";
                        this.srcIP = IP.getIpSrc();
                        this.dstIP = IP.getIpDst();
                        if(!IP.getProtocol().equals("06")){
                            this.protocol = "Unsupported";
                        }
                    }
                    if (trame.size() >= 54) { 
                        TCP = IP.analyseProtocole();
                        if(TCP != null){
                            this.protocol = "TCP";
                            if(TCP.getData() != null){
                                this.protocol = "HTTP";
                            }
                            this.srcPort = TCP.getPortSrc();
                            this.dstPort = TCP.getPortDst();
                            this.data = new Datagramme(this);
                        }
                    } else { 
                        TCP = null;
                        this.protocol = "Unknown";
                    }
                } 
            } else { 
                IP = null; 
            }
        } else { 
            ethernet = null; 
        }
        
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Méthodes et assesseurs
    ------------------------------------------------------------------------------------------------------------------*/

    public Ethernet getEthernet(){
        return ethernet;
    }

    public IP getIP(){
        return IP;
    }

    public TCP getTcp(){
        return TCP;
    }

    public Datagramme getDatagramme(){
        return data;
    }

    public String getIpSrc(){
        return srcIP;
    }

    public String getIpDst(){
        return dstIP;
    }

    public String getPortSrc(){
        return srcPort;
    }

    public String getPortDst(){
        return dstPort;
    }

    public int getNum(){
        return num;
    }
    
    public List<String> getTrame(){
        return trame;
    }
    
    public String getProtocol(){
        return protocol;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (ethernet != null) {
            sb.append(ethernet.toString()).append("\n");
            if (IP != null) {
                sb.append(IP.toString()).append("\n");
                if (TCP != null) {
                    sb.append(TCP.toString()).append("\n");
                    if(TCP.getData() != null){
                        sb.append(TCP.getData());
                    }
                }
            }
        }
        return sb.toString();
    }
    public int getCle(){
        return cle;
    }
}
