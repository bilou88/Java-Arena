import java.util.Scanner;

public class MenuJeu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choix;
        boolean quitter = false;

        while (!quitter) {
            afficherMenu();
            choix = scanner.nextInt();

            switch (choix) {
                case 1:
                    menuPartie(scanner);
                    break;

                case 2:
                    afficherEquipe();
                    break;

                case 3:
                    afficherJoueur();
                    break;

                case 4:
                    ouvrirBoutique();
                    break;

                case 5:
                    System.out.println("Au revoir !");
                    quitter = true;
                    break;

                default:
                    System.out.println("Choix invalide.");
            }
        }

        scanner.close();
    }

    // ===== MENUS =====

    public static void afficherMenu() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1 → Nouvelle partie / Charger une partie");
        System.out.println("2 → Mon équipe");
        System.out.println("3 → Mon joueur");
        System.out.println("4 → Boutique");
        System.out.println("5 → Quitter");
        System.out.print("Votre choix : ");
    }

    public static void menuPartie(Scanner scanner) {
        System.out.println("\n1 → Nouvelle partie");
        System.out.println("2 → Charger une partie");
        System.out.print("Votre choix : ");
        int choix = scanner.nextInt();

        if (choix == 1) {
            System.out.println("Nouvelle partie créée !");
        } else if (choix == 2) {
            System.out.println("Partie chargée !");
        } else {
            System.out.println("Choix invalide.");
        }
    }

    // ===== ACTIONS =====

    public static void afficherEquipe() {
        System.out.println("\n=== MON ÉQUIPE ===");
        System.out.println("Monstre 1 : 80 PV");
        System.out.println("Monstre 2 : 65 PV");
        System.out.println("Monstre 3 : 100 PV");
    }

    public static void afficherJoueur() {
        System.out.println("\n=== MON JOUEUR ===");
        System.out.println("Nom : Joueur1");
        System.out.println("Niveau : 5");
        System.out.println("Or : 250");
    }

    public static void ouvrirBoutique() {
        System.out.println("\n=== BOUTIQUE ===");
        System.out.println("1 → Potion (50 or)");
        System.out.println("2 → Épée (200 or)");
    }
}
