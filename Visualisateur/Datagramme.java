/* ---------------------------------------------------------------------------------------------------------------------
       Package
----------------------------------------------------------------------------------------------------------------------*/

package Visualisateur;

/* ---------------------------------------------------------------------------------------------------------------------
       Importations
----------------------------------------------------------------------------------------------------------------------*/

import Outils.Tools;

/* ---------------------------------------------------------------------------------------------------------------------
       Datagramme de la trame
----------------------------------------------------------------------------------------------------------------------*/

public class Datagramme implements Visualisateur {

    /* -----------------------------------------------------------------------------------------------------------------
        Attributs
    ------------------------------------------------------------------------------------------------------------------*/

    private StringBuilder datagramme;

    /* -----------------------------------------------------------------------------------------------------------------
        Constructeurs
    ------------------------------------------------------------------------------------------------------------------*/

    public Datagramme(Trame trame){
        if(trame.getTcp()!=null){
            datagramme = new StringBuilder();
            if(trame.getTcp().getData() != null){
                datagramme.append(" ___________________________________________________________________________________\n");
                datagramme.append("| Trame n°"+trame.getNum() + " - IPv4 (0x0800) communication with Hypertext Transfert Protocol (HTTP): |\n\n");
                datagramme.append(trame.getIpSrc()+"                    ");
                datagramme.append(trame.getIpDst()+"\n");
                datagramme.append("    "+Tools.getDecValues(trame.getPortSrc()));
                datagramme.append("|-------------------------->|");
                datagramme.append(Tools.getDecValues(trame.getPortDst()));
                datagramme.append("\t* " + infoLine(trame)+" *");
                datagramme.append("\n|___________________________________________________________________________________|\n");
            }else{
                datagramme.append(" __________________________________________________________________________________________________\n");
                datagramme.append("| Trame n°"+trame.getNum() + " - IPv4 (0x0800) communication with protocol TCP (0x06) :                               |\n\n");
                datagramme.append(trame.getIpSrc()+"                    ");
                datagramme.append(trame.getIpDst()+"\n");
                datagramme.append("    "+Tools.getDecValues(trame.getPortSrc()));
                datagramme.append("|-------------------------->|");
                datagramme.append(Tools.getDecValues(trame.getPortDst()));
                datagramme.append("\t* " + infoLine(trame)+" *");
                datagramme.append("\n|__________________________________________________________________________________________________|\n");

                
            }
        }
        else {
            datagramme.append(" ___________________________________________________________________________________\n");
            datagramme.append("| Trame n°"+trame.getNum() + " - Pas de graphique de trafic possible car le protocole n'est pas supporté par notre visualistaeur de trafic. |\n\n");
        }
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Méthodes et assesseurs
    ------------------------------------------------------------------------------------------------------------------*/

    public static String infoLine(Trame trame){
        StringBuilder sb = new StringBuilder();
        if(trame.getTcp()!= null){
            if(trame.getTcp().getData()!= null){
                sb.append("HTTP : "+ trame.getTcp().getData().toStringRequestResponse());
            }else{
                if(!(trame.getTcp().getFlagSet().isEmpty())){
                    sb.append("TCP : "+Tools.getDecValues(trame.getPortSrc())+" -> "+Tools.getDecValues(trame.getPortDst())+" "+trame.getTcp().getFlagSet() + " ");
                    for(int i = 0; i < trame.getTcp().getFlagSet().size() ; i++){
                        if(trame.getTcp().getFlagSet().get(i).equals("URG")){sb.append("Urg = " + trame.getTcp().getUrg()+" ");}
                        if(trame.getTcp().getFlagSet().get(i).equals("ACK")){sb.append("ACK = " + trame.getTcp().getAck()+" ");}
                        if(trame.getTcp().getFlagSet().get(i).equals("PSH")){sb.append("Psh = " + trame.getTcp().getPsh()+" ");}
                        if(trame.getTcp().getFlagSet().get(i).equals("RST")){sb.append("Rst = " + trame.getTcp().getRst()+" ");}
                        if(trame.getTcp().getFlagSet().get(i).equals("SYN")){sb.append("Syn = " + trame.getTcp().getSyn()+" ");}
                        if(trame.getTcp().getFlagSet().get(i).equals("FIN")){sb.append("Urg =" + trame.getTcp().getFin()+" ");}
                    }
                }
                sb.append(" Seq = " + Tools.getDecValues(trame.getTcp().getSeq())+" ");
                sb.append(" Ack = " + Tools.getDecValues(trame.getTcp().getAck())+" ");
                sb.append(" Win = " + Tools.getDecValues(trame.getTcp().getWdw())+" ");
            }

        }
        return sb.toString();
    }
    public String toString(){
        return datagramme.toString();
    }
}
