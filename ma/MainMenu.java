package fr.javaarena.ui;

import fr.javaarena.game.Game;
import java.util.Scanner;

public class MainMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final Game game = new Game();
    private boolean running = true;

    public void start() {
        while (running) {
            afficherMenu();
            int choix = lireEntierSecurise("Ton choix : ");

            switch (choix) {
                case 1 -> menuNouvelleOuCharger();
                case 2 -> game.afficherEquipe();
                case 3 -> menuMonJoueur();
                case 4 -> System.out.println("\n(Boutique à faire plus tard)\n");
                case 5 -> quitter();
                default -> System.out.println("❌ Choix invalide.\n");
            }
        }
    }

    private void afficherMenu() {
        System.out.println("=================================");
        System.out.println("        JAVA ARENA - MENU         ");
        System.out.println("=================================");
        System.out.println("1 -> Nouvelle partie / Charger");
        System.out.println("2 -> Mon équipe");
        System.out.println("3 -> Mon joueur");
        System.out.println("4 -> Boutique");
        System.out.println("5 -> Quitter");
        System.out.println("=================================");
    }

    private void menuNouvelleOuCharger() {
        System.out.println("\n--- Nouvelle / Charger ---");
        System.out.println("1 -> Nouvelle partie");
        System.out.println("2 -> Charger partie");
        System.out.println("3 -> Retour");

        int choix = lireEntierSecurise("Ton choix : ");
        switch (choix) {
            case 1 -> game.nouvellePartie(scanner);
            case 2 -> game.chargerPartie();
            case 3 -> System.out.println();
            default -> System.out.println("❌ Choix invalide.\n");
        }
    }

    private void menuMonJoueur() {
        System.out.println("\n--- Mon joueur ---");
        System.out.println("1 -> Attaquer un monstre");
        System.out.println("2 -> Utiliser inventaire (affichage simple)");
        System.out.println("3 -> Retour");

        int choix = lireEntierSecurise("Ton choix : ");
        switch (choix) {
            case 1 -> game.combat(scanner);
            case 2 -> game.afficherInventaire(scanner);
            case 3 -> System.out.println();
            default -> System.out.println("❌ Choix invalide.\n");
        }
    }

    private void quitter() {
        System.out.println("\nSauvegarder avant de quitter ? (O/N)");
        String rep = scanner.nextLine().trim().toUpperCase();
        if (rep.equals("O")) game.sauvegarderPartie();
        running = false;
        System.out.println("👋 À bientôt !");
    }

    private int lireEntierSecurise(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Merci de saisir un nombre.\n");
            }
        }
    }
}
