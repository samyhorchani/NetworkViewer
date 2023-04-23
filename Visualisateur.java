/* ---------------------------------------------------------------------------------------------------------------------
       Importations des bibliothèques
----------------------------------------------------------------------------------------------------------------------*/

import java.io.*;
import java.awt.Desktop;
import java.util.*;

/* ---------------------------------------------------------------------------------------------------------------------
       Importations des packages
----------------------------------------------------------------------------------------------------------------------*/

import Outils.*;

import Visualisateur.*;

/* ---------------------------------------------------------------------------------------------------------------------
       Point d'entrée du programme
----------------------------------------------------------------------------------------------------------------------*/

public class Visualisateur {
    public static void main(String[] args) throws IOException {

        /* -----------------------------------------------------------------------------------------------------------------
            Menu
        ------------------------------------------------------------------------------------------------------------------*/

        StringBuilder menu = new StringBuilder();
        menu.append("--------------------------------------------------------------------------------------------------------------------------------------------------\n\n");
        menu.append("Quelle action souhaitez-vous réaliser ?\n");
        menu.append("\n\t\t1. Afficher le graphique de trafic.");
        menu.append("\n\t\t2. Afficher une trame.");
        menu.append("\n\t\t3. Filtrer les trames.");
        menu.append("\n\t\t4. Quitter le programme.\n");
        menu.append("\n--------------------------------------------------------------------------------------------------------------------------------------------------\n");
        menu.append("\nPour réaliser une action, veuillez entrer l'option souhaitée.");
        menu.append("\n\tJe souhaite réaliser l'action n°");

        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------\n");
        System.out.println("\t\t\t\t\tBienvenue sur notre visualisateur de trafic réseau !");
        System.out.println("\n--------------------------------------------------------------------------------------------------------------------------------------------------\n");
        System.out.println("Pour analyser votre propre fichier, veuillez entrer son nom. Sinon, entrez 'trafic.txt'.");
        System.out.println("Pour être analysé, votre fichier doit contenir des sauts de lignes entre les trames et chaque trame doit contenir un offset valide.");

        /* -----------------------------------------------------------------------------------------------------------------
            Recherche et lecture de la trame à analyser
        ------------------------------------------------------------------------------------------------------------------*/

        System.out.print("\tJe souhaite ouvrir : ");
        Scanner scanner = new Scanner(System.in);
        String file_name = scanner.nextLine();
        File file;
        file = new File("FichierAAnalyser//" + file_name); 

        System.out.print("\t-> Vous avez choisi d'ouvrir ");
        if (file_name.equals("trafic.txt")) { System.out.println("le trafic proposé avec le programme.");
               
        }
        else { 
            System.out.println("un de vos fichiers."); 
        }

        if (file.exists()) {
            System.out.print("\t-> Analyse des trames disponibles... ");
        }
        else { System.out.println("\t-> Votre fichier n'existe pas à l'adresse : " + file_name); scanner.close(); return; }

        /* -----------------------------------------------------------------------------------------------------------------
            Création du dossier avec les trames analysées
        ------------------------------------------------------------------------------------------------------------------*/
    
        Lecture l = new Lecture(file); //lecture des trames
        if(l.getErrors().isEmpty()){
            System.out.println("Aucune erreur détectée à la lecture. Toutes les trames ont été prises en compte.");
        }else{
            System.out.println("Une/des erreur(s) ont détectée(s) à la lecture => Trames contenant les erreurs ne sont pas prise en compte.\n");
            System.out.println("Erreurs : "+l.getErrors()+"\n");
        }
        Map<Integer, List<String>> trafic = l.getData(); // trames lues dans la map
        List<Integer> cles = l.getKeys(); // création d'une liste de clé
        List<Trame> tabTrames = Tools.tabTrame(trafic, cles); // création d'une liste de trames
        int nbTrames = cles.size();

        System.out.print("\nQuel nom souhaitez-vous donner au dossier qui contiendra l'analyse ?");
        System.out.println(" Veuillez entrer un nom sans espaces.");
        System.out.print("\tJe souhaite le nommer : ");
        String name = scanner.nextLine();
        System.out.println("\t-> Création du dossier " + name + "... Ajout des trames analysées et du datagramme...");

        File dossier1 = new File(name);
        dossier1.mkdir();
        Ecriture.write(name, tabTrames);


        System.out.println("\nCi-dessous, les " + nbTrames + " trames présentes sur le trafic : \n");
        Tools.affichageTramesDispo(tabTrames);

        System.out.print(menu);

        int choix = 0;
        while(choix != 4){ //tant que on exit pas on boucle
            choix = scanner.nextInt();
            if(choix < 1 || choix > 4){
                System.out.println("\nVeuillez rentrez un numéro d'option valide dans la console");
                choix = scanner.nextInt();
            }
            switch(choix){

                /* -----------------------------------------------------------------------------------------------------------------
                    Affichage du graphique du trafic
                ------------------------------------------------------------------------------------------------------------------*/

                case 1:
                    System.out.println("\t-> Vous avez choisi d'afficher le graphique du trafic.");

                    Desktop d1 = Desktop.getDesktop();
                    File data_demande = new File(name+"//datagramme.txt");
                    System.out.println("\t-> Ouverture du datagramme...\n");
                    d1.open(data_demande);

                    System.out.print(menu);
                    break;

                /* -----------------------------------------------------------------------------------------------------------------
                    Affichage d'une trame spécifique
                ------------------------------------------------------------------------------------------------------------------*/

                case 2:
                    System.out.println("\t-> Vous avez choisi d'ouvrir une trame spécifique. Laquelle ?");
                    System.out.print("\t-> Je souhaite analyser la trame n°");
                    int num_trame_a_analyser = scanner.nextInt();
                    while(num_trame_a_analyser<1 || num_trame_a_analyser > nbTrames){
                        System.out.println("\nLa trame que vous avez demandé n'existe pas.");
                        System.out.println("\nVeuillez entrer un numéro de trame compris entre 1 et "+nbTrames+".\n");
                        System.out.print("\tJe souhaite analyser la trame n°");
                        num_trame_a_analyser = scanner.nextInt();
                    }
                    trafic.get(num_trame_a_analyser-1);
                    Desktop d = Desktop.getDesktop();
                    String name_trame_demandee = name + "//trame"+num_trame_a_analyser+".txt";
                    File trame_demandee = new File(name_trame_demandee);
                    System.out.println("\t-> Ouverture de la trame n°" + num_trame_a_analyser + "...\n");
                    d.open(trame_demandee);
                    System.out.print(menu);
                    break;

                /* -----------------------------------------------------------------------------------------------------------------
                    Filtrage des trames du trafic
                ------------------------------------------------------------------------------------------------------------------*/

                case 3:

                    System.out.println("\t-> Vous avez choisi de filtrer votre trafic.");
                    System.out.println("\t-> Ouverture du dictionnaire de filtres...");

                    Desktop d2 = Desktop.getDesktop();
                    File filtres = new File("Visualisateur/DICTIONNAIRE.pdf");
                    d2.open(filtres);

                    System.out.print("\t-> Veuillez entrez votre commande : ");
                    Scanner scanner2 = new Scanner(System.in);
                    String cmd = scanner2.nextLine();

                    while(!Filtres.isFilter(cmd)){
                        System.out.print("\t-> Veuillez entrez votre commande valide (se referer au dictionnaire de filtres): ");
                        d2.open(filtres);
                        cmd = scanner2.nextLine();
                    }

                    System.out.print("\t-> Recherche des trames...");

                    List<Trame> trames_filtrees = Filtres.execFilter(cmd, tabTrames);

                    if (trames_filtrees != null) {
                        Ecriture.write(cmd, trames_filtrees);
                        System.out.println("\n\t->Retrouvez toutes les trames filtrées dans le dossier " + cmd +".\n");

                    }else { System.out.println("Aucune trame n'existe avec ce filtre.\n"); }

                    System.out.print(menu);
                    break;

                /* -----------------------------------------------------------------------------------------------------------------
                    Sortie du programme
                ------------------------------------------------------------------------------------------------------------------*/

                case 4:
                    System.out.println("\n--------------------------------------------------------------------------------------------------------------------------------------------------\n");
                    System.out.println("\t\t\t\tMerci d'avoir utilisé notre visualisateur de trafic réseau. Bonne continuation !\n");
                    System.out.println("\t\tCe visualisateur de trafic réseau a été réalisé par Samy HORCHANI & Maéva DORMANT dans le cadre de l'UE LU3IN033.\n");
                    System.out.println("\t\tNous souhaitions remercier notre enseignant Prométhée SPATHIS ansi que toute l'équipe pédagogique pour leur encadrement.\n");
                    System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");
                    scanner.close();
                    return;
            }
        }
        
    }
}