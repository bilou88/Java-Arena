package src.com.javaArena.util;

import src.com.javaArena.model.Dresseur;
import src.com.javaArena.model.Monstre;
import src.com.javaArena.model.MonstreFeu;
import src.com.javaArena.model.MonstreEau;
import src.com.javaArena.model.MonstrePlante;

import java.io.*;
import java.util.Map;

public class Persistance {
    private static final String SAVE_FILE = "sauvegarde_javaArena.csv";
    
    public static void sauvegarder(Dresseur dresseur) {
        try (FileWriter fw = new FileWriter(SAVE_FILE);
             BufferedWriter bw = new BufferedWriter(fw)) {
            
            bw.write("TYPE,DONNEE,VALEUR\n");
            bw.write("EQUIPE," + dresseur.getNomEquipe() + ",\n");
            bw.write("CREDITS," + dresseur.getCredits() + ",\n");
            
            for (int i = 0; i < dresseur.getEquipe().size(); i++) {
                Monstre monstre = dresseur.getEquipe().get(i);
                bw.write("MONSTRE," + monstre.getType() + "," + monstre.getNom() + "," + 
                         monstre.getPvActuels() + "," + monstre.getPvMax() + "," + 
                         monstre.getPuissanceAttaque() + "\n");
            }
            
            for (Map.Entry<String, Integer> entry : dresseur.getInventaire().entrySet()) {
                bw.write("INVENTAIRE," + entry.getKey() + "," + entry.getValue() + "\n");
            }
            
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }
    
    public static Dresseur charger() {
        Dresseur dresseur = null;
        
        try (BufferedReader br = new BufferedReader(new FileReader(SAVE_FILE))) {
            String ligne;
            br.readLine();
            
            while ((ligne = br.readLine()) != null) {
                String[] parts = ligne.split(",");
                
                if (parts[0].equals("EQUIPE")) {
                    dresseur = new Dresseur(parts[1]);
                } else if (parts[0].equals("CREDITS") && dresseur != null) {
                    dresseur.setCredits(Integer.parseInt(parts[1]));
                } else if (parts[0].equals("MONSTRE") && dresseur != null) {
                    String type = parts[1];
                    String nom = parts[2];
                    int pvActuels = Integer.parseInt(parts[3]);
                    int pvMax = Integer.parseInt(parts[4]);
                    int puissance = Integer.parseInt(parts[5]);
                    
                    Monstre monstre = creerMonstre(type, nom, pvMax, puissance);
                    monstre.setPvActuels(pvActuels);
                    dresseur.ajouterMonstre(monstre);
                } else if (parts[0].equals("INVENTAIRE") && dresseur != null) {
                    String objet = parts[1];
                    int quantite = Integer.parseInt(parts[2]);
                    dresseur.ajouterObjet(objet, quantite);
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement : " + e.getMessage());
            return null;
        }
        
        return dresseur;
    }
    
    private static Monstre creerMonstre(String type, String nom, int pvMax, int puissance) {
        switch (type) {
            case "Feu":
                return new MonstreFeu(nom, pvMax, puissance);
            case "Eau":
                return new MonstreEau(nom, pvMax, puissance);
            case "Plante":
                return new MonstrePlante(nom, pvMax, puissance);
            default:
                return new MonstreFeu(nom, pvMax, puissance);
        }
    }
    
    public static boolean fichierExiste() {
        return new File(SAVE_FILE).exists();
    }
}