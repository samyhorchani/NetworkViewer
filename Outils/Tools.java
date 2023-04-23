/* ---------------------------------------------------------------------------------------------------------------------
       Package
----------------------------------------------------------------------------------------------------------------------*/

package Outils;

/* ---------------------------------------------------------------------------------------------------------------------
       Importations
----------------------------------------------------------------------------------------------------------------------*/

import Visualisateur.Trame;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.math.BigInteger;
import Visualisateur.Datagramme;

/* ---------------------------------------------------------------------------------------------------------------------
       Ensemble des méthodes permettant d'analyser des String, ou des trames
----------------------------------------------------------------------------------------------------------------------*/

public class Tools implements Outil {

    /* -----------------------------------------------------------------------------------------------------------------
        Permet de convertir une valeur hexadécimale en décimale
    ------------------------------------------------------------------------------------------------------------------*/

    public static long getDecValues(String hex){ return Long.parseLong(hex, 16);}

    /* -----------------------------------------------------------------------------------------------------------------
        Permet de convertir un ASCII en chaîne de caractères
    ------------------------------------------------------------------------------------------------------------------*/

    public static String asciiToString(String ascii){
        StringBuilder result = new StringBuilder();
        int mod = 1;
        String word = "";
        for(String s : ascii.split("")){
            word+=s;
            if((mod%2)==0){
                result.append((char) Integer.parseInt(word, 16));
                word = "";
                mod = 0;
            }
            mod++;
        }
        return result.toString();
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Permet de convertir une valeur hexadécimale en binaire
    ------------------------------------------------------------------------------------------------------------------*/

    public static String hexToBin(String hex) {
        hex = hex.toUpperCase();
        hex = hex.replaceAll("0", "0000");
        hex = hex.replaceAll("1", "0001");
        hex = hex.replaceAll("2", "0010");
        hex = hex.replaceAll("3", "0011");
        hex = hex.replaceAll("4", "0100");
        hex = hex.replaceAll("5", "0101");
        hex = hex.replaceAll("6", "0110");
        hex = hex.replaceAll("7", "0111");
        hex = hex.replaceAll("8", "1000");
        hex = hex.replaceAll("9", "1001");
        hex = hex.replaceAll("A", "1010");
        hex = hex.replaceAll("B", "1011");
        hex = hex.replaceAll("C", "1100");
        hex = hex.replaceAll("D", "1101");
        hex = hex.replaceAll("E", "1110");
        hex = hex.replaceAll("F", "1111");
        return hex;
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Permet de récupérer la valeur entière d'un hexadécimal
    ------------------------------------------------------------------------------------------------------------------*/

    public static BigInteger getIntValue(String hex) {
        return new BigInteger(hex, 16);
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Permet de déterminer si le binaire est "set" ou "not set"
    ------------------------------------------------------------------------------------------------------------------*/

    public static String isSet(String bin){
        if(Integer.parseInt(bin, 2) == 0){
            return "Not set";
        }
        return "Set";
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Permet de créer une liste de trames
    ------------------------------------------------------------------------------------------------------------------*/

    public static List<Trame> tabTrame(Map<Integer, List<String>> trame, List<Integer> keys){
        List<Trame> tabTrame = new ArrayList<>();
        for(int i = 0; i < trame.size(); i++){
            tabTrame.add(new Trame(i+1, keys.get(i), trame.get(keys.get(i))));
        }

        return tabTrame;
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Permet d'afficher un datagramme
    ------------------------------------------------------------------------------------------------------------------*/

    public static void afficheDatagramme(List<Trame> trames){
        for(int i = 0; i < trames.size() ; i++){
            if(trames.get(i).getDatagramme() != null){
                System.out.println(trames.get(i).getDatagramme());
            }
        }
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Permet d'afficher les trames disponibles (valides)
    ------------------------------------------------------------------------------------------------------------------*/

    public static void affichageTramesDispo(List<Trame> trames){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < trames.size() ; i++){
            sb.append("Trame n° "+(i+1)+"\t");
            if(trames.get(i).getIpSrc() != null && trames.get(i).getIpDst() != null){
                sb.append("Ip Source : "+trames.get(i).getIpSrc()+"\t");
                if(trames.get(i).getIpSrc().equals("0.0.0.0")){sb.append("\t"); } //regler bug affichage
                sb.append("Ip Destination : "+trames.get(i).getIpDst()+"\t");
                sb.append("Protocol : "+trames.get(i).getProtocol()+"\n");
            }else{
                sb.append("Ip Source : No Ip\t\tIp Destination : No Ip\t\t");
                sb.append("Protocol : "+trames.get(i).getProtocol()+"\n");
            }

        }
        if(sb.length() != 0){
            System.out.println(sb.toString());
        }else{
            System.out.println("Aucune trames à afficher.\n");
        }
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Permet de récupérer le graphique de trafic
    ------------------------------------------------------------------------------------------------------------------*/

    public static String printFlowGraph(List<Trame> trames){
        StringBuilder result = new StringBuilder();
        result.append("Voici le graphique de trafic que nous déduisons des trames valides fournies : \n\n");
        StringBuilder sb = new StringBuilder();

        List<Trame> clone = new ArrayList<>();
        clone.addAll(trames);
        int j;
        for(int i = 0; i < trames.size() ; i++){
            String ipSrc = trames.get(i).getIpSrc();
            String ipDst = trames.get(i).getIpDst();
            int cpt = 0;
            for(j = 0; j < clone.size();j++){
                if(clone.get(j).getTcp() != null){
                    if(cpt == 0 && !trames.get(i).getProtocol().equals("Unsupported") && ipSrc != null && ipDst != null){
                        if( !(ipSrc.equals(clone.get(j).getIpDst()) && ipDst.equals(clone.get(j).getIpSrc()))){
                            sb.append(ipSrc+"                    ");
                            sb.append(ipDst+"\n");
                            cpt++;
                        }
                    }
                    if(ipSrc != null  && ipDst != null && clone.get(j).getIpSrc() != null && clone.get(j).getIpDst() != null){
                        if( ipSrc.equals(clone.get(j).getIpSrc()) && ipDst.equals(clone.get(j).getIpDst()) ){
                            sb.append("    "+Tools.getDecValues(clone.get(j).getPortSrc()));
                            sb.append("|-------------------------->|");
                            sb.append(Tools.getDecValues(clone.get(j).getPortDst()));
                            sb.append("\t* " + Datagramme.infoLine(clone.get(j))+" *\n\n");
                        }if( ipSrc.equals(clone.get(j).getIpDst()) && ipDst.equals(clone.get(j).getIpSrc()) ){
                            sb.append("    "+Tools.getDecValues(clone.get(j).getPortSrc()));
                            sb.append("|<--------------------------|");
                            sb.append(Tools.getDecValues(clone.get(j).getPortDst()));
                            sb.append("\t* " + Datagramme.infoLine(clone.get(j))+" *\n\n");
                        }
                        clone.remove(j);
                    }
                }
            }
            if(sb.length() != 0){
                result.append(sb.toString()+"\n");
            }
            sb.setLength(0);
        }
        return result.toString();
    }

}