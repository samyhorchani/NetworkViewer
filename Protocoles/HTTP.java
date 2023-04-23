/* ---------------------------------------------------------------------------------------------------------------------
       Package
----------------------------------------------------------------------------------------------------------------------*/

package Protocoles;

/* ---------------------------------------------------------------------------------------------------------------------
       Importations
----------------------------------------------------------------------------------------------------------------------*/

import java.util.ArrayList;
import java.util.List;
import Outils.*;

/* ---------------------------------------------------------------------------------------------------------------------
       Protocole HTTP
----------------------------------------------------------------------------------------------------------------------*/

public class HTTP implements Protocole {

    /* -----------------------------------------------------------------------------------------------------------------
        Attributs
    ------------------------------------------------------------------------------------------------------------------*/

    private List<String> message;
    private boolean isRequest; //savoir si c'est une requete ou non
    private HTTP_Request requestLine;
    private HTTP_Response responseLine;
    private String corps = "";
    private int index; // élément courant que l'on parcourt
    private List<Champ> champs;
    private String corpsPOST;

    /* -----------------------------------------------------------------------------------------------------------------
        Constructeurs
    ------------------------------------------------------------------------------------------------------------------*/

    public HTTP(List<String> message, boolean isRequest){
        this.message = message;
        this.isRequest = isRequest;
        this.champs = new ArrayList<>();
        this.index = 0;

        if(isRequest){
            request();
        }
        else{
            response();
        }
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Constructeurs
    ------------------------------------------------------------------------------------------------------------------*/

    public static String getMethode(String hex){
        hex = hex.toUpperCase();
        if (hex.equals("474554")) return "GET";
        if (hex.equals("48454144")) return "HEAD";
        if (hex.equals("505F5354")) return "POST";
        if (hex.equals("505554")) return "PUT";
        if (hex.equals("44454C455445")) return "DELETE";
        return "none";
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Méthodes et assesseurs
    ------------------------------------------------------------------------------------------------------------------*/

    private String getLine(){ //extrait une ligne
        StringBuilder result = new StringBuilder();
        String value;
        while(true){
                if(!(index < message.size())){ break;}
                value = message.get(index++);
                if(value.toLowerCase().equals("0d") && message.get(index).toLowerCase().equals("0a")){
                    result.append(value);
                    result.append(message.get(index));
                    index++;
                    return result.toString();
                }
            result.append(value);
        }
        return result.toString();
    }

    private void requestLine(){ //crée une ligne de requete
        String line = getLine();
        String[] tab = line.toLowerCase().split("20");
        String methode = tab [0];
        String url = tab[1];
        String version = tab[2].substring(0, tab[2].length() - 4);
        this.requestLine = new HTTP_Request(methode, url, version);
    }
    
    private boolean isLast(String ligne) { //verifie si derniere ligne
        return ligne.toLowerCase().equals("0d0a");
    }

    private void extractChamp(){
        String line = "";
        while(true){
            line = getLine();
            if(isLast(line)){
                return;
            }
            this.champs.add(extractChampFromLine(line));
        }
    }

    private Champ extractChampFromLine(String line){
        line = Tools.asciiToString(line);
        String tab[] = line.split(":");
        String nomChamp = tab[0];
        String valeur = tab[1];
        for (int i = 2; i < tab.length; i++) {
            valeur = valeur + ":" + tab[i];
        }

        return new Champ(nomChamp, valeur);
    }

    private void requestPost(){
        if (!getMethode(this.requestLine.getMethode()).equals("POST")) {
            return;
        }
        StringBuilder sb = new StringBuilder();

        for (; index < message.size(); index++){
            sb.append(message.get(index));
        }

        this.corpsPOST = Tools.asciiToString(sb.toString());
    }

    private void request(){
        requestLine();
        extractChamp();
        requestPost();
    }

    private void response(){
        responseLine();
        extractChamp();
        responsePost();
    }

    private void responseLine(){
        String line = getLine();
        String[] tab = line.split("20");
        try{
            this.responseLine = new HTTP_Response(tab[0], tab[1], tab[2]);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void responsePost(){
        StringBuilder sb = new StringBuilder();
        for(; index < message.size(); index++){
            sb.append(message.get(index));
        }
        this.corps = Tools.asciiToString(sb.toString());
    }

    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\nHypertext Transfer Protocol\n\t");
        sb.append(toStringRequestResponse());
        sb.append("\n");
        for(Champ ch : champs){
            sb.append("\t");
            sb.append(ch);
        }
        if (isRequest && requestLine.getMethode().toUpperCase().equals("POST")){
            sb.append(corpsPOST);
        }
        if (!isRequest){
            sb.append(corps);
        }
        return sb.toString();
    }
    public String toStringRequestResponse(){
        StringBuilder sb = new StringBuilder();
        if(isRequest){
            sb.append(this.getMethode(requestLine.getMethode()) + " ");
            sb.append(Tools.asciiToString(requestLine.getUrl()));
            sb.append(Tools.asciiToString(requestLine.getVersion()));
        }else{
            
            sb.append("Version : "+responseLine.getVersion() + " ");
            sb.append(" Status Code : " + responseLine.getStatusCode());
            sb.append(" Message :" + Tools.asciiToString(responseLine.getMessage()));
            
        }
        return sb.toString();
    }
    public boolean getIsRequest(){
        return isRequest;
    }
    
}