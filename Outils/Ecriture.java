/* ---------------------------------------------------------------------------------------------------------------------
       Package
----------------------------------------------------------------------------------------------------------------------*/

package Outils;

/* ---------------------------------------------------------------------------------------------------------------------
    Importations
----------------------------------------------------------------------------------------------------------------------*/

import java.io.*;
import java.util.List;

import Visualisateur.Filtres;
import Visualisateur.Trame;

/* ---------------------------------------------------------------------------------------------------------------------
    Ensemble des méthodes permettant l'écriture dans un fichier
----------------------------------------------------------------------------------------------------------------------*/

public class Ecriture implements Outil {

    /* -----------------------------------------------------------------------------------------------------------------
        Permet d'écrire une liste de trames et un datagramme dans un dossier
    ------------------------------------------------------------------------------------------------------------------*/

    public static void write(String folder_name, List<Trame> trame){
        File folder = new File(folder_name);
        if(!folder.exists()){
            folder.mkdir();
        }
        writeTxtFiles(folder_name, trame);
        writeTxtDatagramme(folder_name, trame);
        
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Permet d'écrire une liste de trames dans un dossier
    ------------------------------------------------------------------------------------------------------------------*/

    private static void writeTxtFiles(String folder_name,List<Trame> trames){
        for(int i = 0; i < trames.size(); i++){
            String file_name = folder_name+"//"+"trame"+(trames.get(i).getNum())+".txt";
            File file = new File(file_name);
            if(!file.exists()){
                try{
                    file.createNewFile();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            try{
                FileWriter writer = new FileWriter(file); //celui du trame.txt
                BufferedWriter bw = new BufferedWriter(writer);
                bw.write("\t\t\t\t\tTrame n°"+(i+1)+" : \n");
                if(trames.get(i).getDatagramme() != null){
                    bw.write("\nFLOW GRAPH : \n");
                    bw.write(trames.get(i).getDatagramme().toString());
                }
                bw.write("\n____________________________________________________________________________\n");
                bw.write("\nDECODAGE : \n");
                bw.write(trames.get(i).toString());
                bw.write("\n____________________________________________________________________________\n");
                bw.write("RAW FRAME :");
                bw.write(trames.get(i).getTrame().toString());
                bw.close();
                writer.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /* -----------------------------------------------------------------------------------------------------------------
        Permet d'écrire un datagramme dans un dossier à partir d'une liste de trames dans un dossier
    ------------------------------------------------------------------------------------------------------------------*/
    public static void writeTxtDatagramme(String folder_name,List<Trame> trames){
        if(!Filtres.isFilter(folder_name)){
            String file_name = folder_name+"//"+"datagramme.txt";
            File file = new File(file_name);
            if(!file.exists()){
                try{
                    file.createNewFile();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            try{
                FileWriter writer = new FileWriter(file); //celui du trame.txt
                BufferedWriter bw = new BufferedWriter(writer);
                
                    bw.write(Tools.printFlowGraph(trames));
                bw.close();
                writer.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
        
}
