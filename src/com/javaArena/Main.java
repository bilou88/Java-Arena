package src.com.javaArena;

import src.com.javaArena.model.Dresseur;
import src.com.javaArena.model.Monstre;
import src.com.javaArena.model.Combat;
import src.com.javaArena.util.MonstreFactory;
import src.com.javaArena.util.Persistance;
import src.com.javaArena.util.Boutique;
import src.com.javaArena.exception.MonstreException;
import src.com.javaArena.exception.MonstreKOException;
import src.com.javaArena.exception.CombatTermineException;
import src.com.javaArena.exception.CaptureImpossibleException;

import java.util.Scanner;

public class Main {
    private static Dresseur dresseur;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        menuPrincipal();
    }
    
    private static void menuPrincipal() {
        boolean quitter = false;
        
        while (!quitter) {
            System.out.println("\n=== JAVA ARENA ===");
            System.out.println("1 -> Commencer une nouvelle partie");
            System.out.println("2 -> Charger une partie existante");
            System.out.println("3 -> Quitter");
            System.out.print("Choix : ");
            
            String choix = lireSaisie();
            
            switch (choix) {
                case "1":
                    nouvellePartie();
                    break;
                case "2":
                    chargerPartie();
                    break;
                case "3":
                    quitter = true;
                    break;
                default:
                    System.out.println("Choix invalide");
            }
        }
        
        System.out.println("Au revoir !");
    }
    
    private static void nouvellePartie() {
        System.out.print("Nom de l'équipe : ");
        String nomEquipe = scanner.nextLine();
        dresseur = new Dresseur(nomEquipe);
        
        for (int i = 0; i < 3; i++) {
            dresseur.ajouterMonstre(MonstreFactory.creerMonstresAleatoire());
        }
        
        dresseur.ajouterObjet("Potion", 1);
        dresseur.ajouterObjet("Pokéball", 5);
        
        menuJeu();
    }
    
    private static void chargerPartie() {
        if (!Persistance.fichierExiste()) {
            System.out.println("Aucune sauvegarde trouvée");
            return;
        }
        
        dresseur = Persistance.charger();
        if (dresseur == null) {
            System.out.println("Erreur lors du chargement");
            return;
        }
        
        System.out.println("Sauvegarde chargée !");
        menuJeu();
    }
    
    private static void menuJeu() {
        boolean quitter = false;
        
        while (!quitter) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1 -> Lancer un combat");
            System.out.println("2 -> Mon équipe");
            System.out.println("3 -> Boutique");
            System.out.println("4 -> Mon joueur");
            System.out.println("5 -> Quitter (avec sauvegarde)");
            System.out.print("Choix : ");
            
            String choix = lireSaisie();
            
            switch (choix) {
                case "1":
                    lancerCombat();
                    break;
                case "2":
                    afficherEquipe();
                    break;
                case "3":
                    gererBoutique();
                    break;
                case "4":
                    afficherJoueur();
                    break;
                case "5":
                    Persistance.sauvegarder(dresseur);
                    System.out.println("Partie sauvegardée");
                    quitter = true;
                    break;
                default:
                    System.out.println("Choix invalide");
            }
        }
    }
    
    private static void lancerCombat() {
        if (dresseur.compterMonstresVivants() == 0) {
            System.out.println("Vous n'avez aucun monstre vivant !");
            return;
        }
        
        Monstre monstresauvage = MonstreFactory.creerMonstresAleatoire();
        Combat combat = new Combat(dresseur, monstresauvage);
        
        System.out.println("\nUn " + monstresauvage.getType() + " sauvage apparaît : " + 
                          monstresauvage.getNom() + " (" + monstresauvage.getPvMax() + " PV)");
        
        Monstre monstreActuel = null;
        for (Monstre m : dresseur.getEquipe()) {
            if (!m.estKO()) {
                monstreActuel = m;
                combat.setMonstreActuel(monstreActuel);
                break;
            }
        }
        
        if (monstreActuel == null) {
            System.out.println("Aucun monstre disponible pour combattre");
            return;
        }
        
        while (!combat.isCombatTermine()) {
            System.out.println("\n" + monstreActuel.getNom() + " (" + monstreActuel.getPvActuels() + 
                              "/" + monstreActuel.getPvMax() + " PV) VS " + 
                              monstresauvage.getNom() + " (" + monstresauvage.getPvActuels() + 
                              "/" + monstresauvage.getPvMax() + " PV)");
            
            System.out.println("1 -> Attaquer");
            System.out.println("2 -> Changer de monstre");
            System.out.println("3 -> Utiliser objet");
            System.out.println("4 -> Capturer");
            System.out.print("Choix : ");
            
            String choix = lireSaisie();
            
            try {
                switch (choix) {
                    case "1":
                        combat.attaquerMonstre();
                        monstreActuel = combat.getMonstreActuel();
                        if (combat.isCombatTermine()) {
                            System.out.println(monstresauvage.getNom() + " est KO !");
                            System.out.println("Vous avez remporté le combat ! +10 crédits");
                        }
                        break;
                    case "2":
                        changerMonstreCombat(combat, dresseur);
                        monstreActuel = combat.getMonstreActuel();
                        break;
                    case "3":
                        utiliserObjetCombat(combat, dresseur, monstreActuel);
                        monstreActuel = combat.getMonstreActuel();
                        break;
                    case "4":
                        if (combat.capturerMonstre()) {
                            System.out.println("Vous avez capturé " + monstresauvage.getNom() + " !");
                        } else {
                            System.out.println("La capture a échoué !");
                        }
                        break;
                    default:
                        System.out.println("Choix invalide");
                }
            } catch (MonstreKOException | CombatTermineException | CaptureImpossibleException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
            
            if (monstreActuel != null && monstreActuel.estKO()) {
                System.out.println(monstreActuel.getNom() + " est KO !");
                boolean tousKO = true;
                for (Monstre m : dresseur.getEquipe()) {
                    if (!m.estKO()) {
                        tousKO = false;
                        break;
                    }
                }
                
                if (tousKO) {
                    System.out.println("Tous vos monstres sont KO ! Vous avez perdu !");
                    combat.setCombatTermine(true);
                    break;
                }
            }
        }
    }
    
    private static void changerMonstreCombat(Combat combat, Dresseur dresseur) {
        System.out.println("\n=== CHANGER DE MONSTRE ===");
        for (int i = 0; i < dresseur.getEquipe().size(); i++) {
            Monstre m = dresseur.getEquipe().get(i);
            String status = m.estKO() ? "[KO]" : "[" + m.getPvActuels() + "/" + m.getPvMax() + " PV]";
            System.out.println((i + 1) + " -> " + m.getNom() + " (" + m.getType() + ") " + status);
        }
        System.out.print("Choix : ");
        
        String choix = lireSaisie();
        try {
            int index = Integer.parseInt(choix) - 1;
            if (index >= 0 && index < dresseur.getEquipe().size()) {
                Monstre nouveau = dresseur.getEquipe().get(index);
                combat.changerMonstre(nouveau);
                combat.setMonstreActuel(nouveau);
            }
        } catch (NumberFormatException | MonstreKOException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
    
    private static void utiliserObjetCombat(Combat combat, Dresseur dresseur, Monstre monstreActuel) {
        System.out.println("\n=== UTILISER OBJET ===");
        for (String objet : dresseur.getInventaire().keySet()) {
            System.out.println(objet + " x" + dresseur.getInventaire().get(objet));
        }
        System.out.print("Objet : ");
        String objet = scanner.nextLine();
        
        try {
            if (objet.equals("Potion")) {
                if (dresseur.utiliserObjet("Potion", 1)) {
                    combat.utiliserPotion(1, monstreActuel);
                    System.out.println(monstreActuel.getNom() + " a été soigné !");
                } else {
                    System.out.println("Vous n'avez pas de Potion");
                }
            } else if (objet.equals("Potion Ressurection")) {
                System.out.println("Quel monstre ressusciter ?");
                for (int i = 0; i < dresseur.getEquipe().size(); i++) {
                    Monstre m = dresseur.getEquipe().get(i);
                    System.out.println((i + 1) + " -> " + m.getNom());
                }
                System.out.print("Choix : ");
                int index = Integer.parseInt(scanner.nextLine()) - 1;
                if (dresseur.utiliserObjet("Potion Ressurection", 1)) {
                    Monstre ressuscite = dresseur.getEquipe().get(index);
                    ressuscite.setPvActuels(ressuscite.getPvMax());
                    System.out.println(ressuscite.getNom() + " a été ressuscité !");
                }
            }
        } catch (MonstreException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
    
    private static void afficherEquipe() {
        System.out.println("\n=== MON ÉQUIPE ===");
        for (int i = 0; i < dresseur.getEquipe().size(); i++) {
            Monstre m = dresseur.getEquipe().get(i);
            String status = m.estKO() ? "[KO]" : "[" + m.getPvActuels() + "/" + m.getPvMax() + " PV]";
            System.out.println((i + 1) + " -> " + m.getNom() + " (" + m.getType() + ") " + status);
        }
    }
    
    private static void gererBoutique() {
        boolean quitter = false;
        
        while (!quitter) {
            Boutique.afficherBoutique();
            System.out.println("Crédits : " + dresseur.getCredits());
            System.out.println("\nOu acheter ? (ou 'retour')");
            System.out.print("Objet : ");
            
            String objet = scanner.nextLine();
            if (objet.equals("retour")) {
                quitter = true;
                break;
            }
            
            System.out.print("Quantité : ");
            String qte = lireSaisie();
            try {
                int quantite = Integer.parseInt(qte);
                Boutique.acheter(dresseur, objet, quantite);
            } catch (NumberFormatException e) {
                System.out.println("Quantité invalide");
            }
        }
    }
    
    private static void afficherJoueur() {
        System.out.println("\n=== MON JOUEUR ===");
        System.out.println("Équipe : " + dresseur.getNomEquipe());
        System.out.println("Crédits : " + dresseur.getCredits());
        System.out.println("\nInventaire :");
        for (String objet : dresseur.getInventaire().keySet()) {
            System.out.println("- " + objet + " x" + dresseur.getInventaire().get(objet));
        }
    }
    
    private static String lireSaisie() {
        try {
            return scanner.nextLine();
        } catch (Exception e) {
            scanner.nextLine();
            return "";
        }
    }
}