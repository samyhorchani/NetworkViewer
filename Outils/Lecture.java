/* ---------------------------------------------------------------------------------------------------------------------
       Package
----------------------------------------------------------------------------------------------------------------------*/

package Outils;

/* ---------------------------------------------------------------------------------------------------------------------
    Importations
----------------------------------------------------------------------------------------------------------------------*/

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/* ---------------------------------------------------------------------------------------------------------------------
    Ensemble des méthodes permettant la lecture d'un un fichier
----------------------------------------------------------------------------------------------------------------------*/

public class Lecture implements Outil {
    private Integer nbLigne;
    private Map<Integer, List<String>> data; // contient chaque ligne du fichier avec la clé la ligne ou elle commence
    private List<Error> errors;
    public static final String ERROR_LIGNE_VIDE = "Ligne incomplète";
    public static final String ERROR_CARACTERE_NON_HEX = "Un Caractère non hex dans une ligne";
    private List<Integer> keys;

    /* -----------------------------------------------------------------------------------------------------------------
        Constructeur
    ------------------------------------------------------------------------------------------------------------------*/

    public Lecture(File file){
        nbLigne = 0;
        this.data = new HashMap<>();
        this.errors = new ArrayList<>();
        this.keys = new ArrayList<>();
        lire(file);
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Permet de lire un fichier
    ------------------------------------------------------------------------------------------------------------------*/

    private void lire(File file) {
        String courantline;
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(file));
            while ((courantline = saut(br)) != null) {
                this.lectureTrameCourante(courantline, br);
            }
            br.close();
        } catch (IOException e) {
        }

    }
    
    public Integer getNbLigne() { return nbLigne; }
    public List<Error> getErrors(){ return errors; }

    public Map<Integer, List<String>> getData(){ return data; }

    /* -----------------------------------------------------------------------------------------------------------------
        Permet de déterminer si une chaîne de caractères est un octet
    ------------------------------------------------------------------------------------------------------------------*/
    private boolean isOctet(String str) {
        if(str.length() != 2) return false;
        try {
            Integer.parseInt(str, 16);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Permet de récupérer l'offset de la ligne
    ------------------------------------------------------------------------------------------------------------------*/

    private String getOffset(String ligne){
        if(ligne.length()>=4){
            String offset = ligne.substring(0, 4); //chaine contenant l'offset
            if(validateOffset(offset))
                return offset;
        }
        return null; //si offset pas valide
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Permet de déterminer si l'offset est valide
    ------------------------------------------------------------------------------------------------------------------*/

    private boolean validateOffset(String offset){ //verifie si offset valide
        try{
            Integer.parseInt(offset, 16);
        }catch(Exception e){
            return false;
        }
        return true;
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Permet de déterminer s'il y a un saut de ligne
    ------------------------------------------------------------------------------------------------------------------*/

    private String saut(BufferedReader br) throws IOException{
        String line;
        String offset;

        while((line = br.readLine()) != null){ //on lit chaque ligne du fichier
            this.nbLigne++;
            if(line.length()>4){ 
                offset = line.substring(0, 4);
                if(validateOffset(offset) && Tools.getDecValues(offset)==0){ //si la ligne a un offset valide on la retourne
                    return line;
                }
            }
        }
        return null;
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Permet de récupérer une ligne sans l'offset
    ------------------------------------------------------------------------------------------------------------------*/

    private String removeOffset(String s)throws Exception{
        if (s.length() <= 7){ //4 char offset + 3 espaces
            throw new Exception();
        }
        return s.substring(7); //sinon on retourne la ligne sans l'offset
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Permet de lore la trame courante
    ------------------------------------------------------------------------------------------------------------------*/

    private void lectureTrameCourante(String ligne, BufferedReader br) throws IOException {

        String prec, courant, octet;
        prec = ligne;
        List<String> trameCourante = new ArrayList<>();
        Integer ligneDebut = nbLigne;
        long cpt;
        String[] octets;
        while ((courant = br.readLine()) != null) {

            this.nbLigne++;

            if (this.getOffset(courant) == null) {
                try {
                    octets = removeOffset(prec).split(" ");
                } catch (Exception e) {
                    return;
                }
                for (String s : octets) {
                    if (!isOctet(s)) break;
                    trameCourante.add(s);
                }
                if (!trameCourante.isEmpty()) {
                    data.put(ligneDebut, trameCourante);
                    this.keys.add(ligneDebut);
                }
                return;
            }
            cpt = Tools.getDecValues(this.getOffset(courant));
            try {
                octets = removeOffset(prec).split(" ");
            } catch (Exception e) {
                this.errors.add(new Error(nbLigne - 1, "Ligne incomplète"));
                return;
            }
            if (cpt < trameCourante.size()) {
                this.errors.add(new Error(nbLigne - 1, "Offset non valide"));
                return;
            }
            try {
                cpt = cpt - trameCourante.size();
                for (int i = 0; i < cpt; i++) {
                    octet = octets[i];
                    if (isOctet(octet)) {
                        trameCourante.add(octet);
                    } else {
                        this.errors.add(new Error(nbLigne - 1, ERROR_CARACTERE_NON_HEX));
                        return;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                this.errors.add(new Error(nbLigne - 1, ERROR_LIGNE_VIDE));
                return;
            }
            prec = courant;
        }
        try {
            octets = removeOffset(prec).split(" ");
        } catch (Exception e) {
            return;
        }
        for (String s : octets) {
            if (!isOctet(s)) break;
            trameCourante.add(s);
        }
        if (!trameCourante.isEmpty()) {
            data.put(ligneDebut, trameCourante);
            this.keys.add(ligneDebut);
        }
    }

    public List<Integer> getKeys(){
        return keys;
    }
    
}