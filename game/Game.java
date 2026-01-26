package fr.javaarena.game;

import fr.javaarena.model.*;
import fr.javaarena.persistence.SaveManager;

import java.util.Random;
import java.util.Scanner;

public class Game {
    private Player player;
    private final SaveManager saveManager = new SaveManager();

    public void nouvellePartie(Scanner scanner) {
        System.out.print("Nom de ton équipe : ");
        String nom = scanner.nextLine().trim();
        player = Player.newPlayerWithRandomTeam(nom);
        System.out.println("✅ Nouvelle partie créée !\n");
    }

    public void chargerPartie() {
        player = saveManager.load();
        System.out.println(player == null ? "❌ Aucune sauvegarde.\n" : "✅ Partie chargée !\n");
    }

    public void sauvegarderPartie() {
        if (player == null) {
            System.out.println("❌ Pas de partie à sauvegarder.\n");
            return;
        }
        saveManager.save(player);
        System.out.println("✅ Partie sauvegardée.\n");
    }

    public void afficherEquipe() {
        if (player == null) {
            System.out.println("❌ Commence ou charge une partie d'abord.\n");
            return;
        }
        player.printTeamStatus();
        System.out.println();
    }

    public void afficherInventaire(Scanner scanner) {
        if (player == null) {
            System.out.println("❌ Commence ou charge une partie d'abord.\n");
            return;
        }

        System.out.println("\n--- Inventaire ---");
        if (player.getInventory().isEmpty()) System.out.println("Vide");
        else player.getInventory().forEach((n, q) -> System.out.println("- " + n + " : " + q));
        System.out.println("\nAppuie sur Entrée pour revenir...");
        scanner.nextLine();
        System.out.println();
    }

    // ======== COMBAT ========
    public void combat(Scanner scanner) {
        if (player == null) {
            System.out.println("❌ Commence ou charge une partie d'abord.\n");
            return;
        }

        if (player.getTeam().stream().allMatch(Monster::isKo)) {
            System.out.println("❌ Tous tes monstres sont KO.\n");
            return;
        }

        Monster wild = randomWild();
        Monster active = firstAlive();

        System.out.println("\n⚔️ Un monstre sauvage apparaît : " + wild.getName() + " (" + wild.getElement() + ")\n");

        boolean fight = true;
        while (fight) {
            afficherPhaseCombat(active, wild);

            System.out.println("1 -> Attaquer un monstre");
            System.out.println("2 -> Changer de monstre");
            System.out.println("3 -> Capturer (que si PV inférieur à 30%)");

            int choix = lireEntier(scanner, "Ton choix : ");

            switch (choix) {
                case 1 -> {
                    int dmg = calculDegats(active, wild);
                    wild.setHp(wild.getHp() - dmg);
                    System.out.println("✅ " + active.getName() + " inflige " + dmg + " dégâts !");

                    if (wild.isKo()) {
                        System.out.println("🏆 Le monstre sauvage est KO !");
                        fight = false;
                        break;
                    }

                    int dmgBack = calculDegats(wild, active);
                    active.setHp(active.getHp() - dmgBack);
                    System.out.println("⚠️ " + wild.getName() + " riposte et inflige " + dmgBack + " dégâts !");

                    if (active.isKo()) {
                        System.out.println("💀 " + active.getName() + " est KO !");
                        if (player.getTeam().stream().anyMatch(m -> !m.isKo())) {
                            System.out.println("➡️ Tu dois changer de monstre !");
                            Monster nouveau = choisirMonstre(scanner);
                            if (nouveau != null) active = nouveau;
                        } else {
                            System.out.println("❌ Tous tes monstres sont KO. Combat perdu.");
                            fight = false;
                        }
                    }
                }

                case 2 -> {
                    Monster nouveau = choisirMonstre(scanner);
                    if (nouveau != null) active = nouveau;
                }

                case 3 -> {
                    double ratio = (double) wild.getHp() / wild.getMaxHp();
                    if (ratio > 0.30) {
                        System.out.println("❌ Capture impossible (> 30% PV).\n");
                        break;
                    }
                    int outils = player.getInventory().getOrDefault("OutilCapture", 0);
                    if (outils <= 0) {
                        System.out.println("❌ Tu n'as pas d'OutilCapture.\n");
                        break;
                    }
                    player.getInventory().put("OutilCapture", outils - 1);
                    System.out.println("🎯 Capture réussie ! " + wild.getName() + " rejoint ton équipe.");
                    player.getTeam().add(wild);
                    fight = false;
                }

                default -> System.out.println("❌ Choix invalide.\n");
            }
        }
        System.out.println();
    }

    private Monster choisirMonstre(Scanner scanner) {
        while (true) {
            System.out.println("\nCHOIX :");
            for (int i = 0; i < player.getTeam().size(); i++) {
                Monster m = player.getTeam().get(i);
                String pvTxt = m.isKo() ? "KO" : (m.getHp() + " PV");
                System.out.println((i + 1) + ") " + m.getName() + " - " + pvTxt + " - " + m.getElement());
            }
            System.out.println("0 -> Retour");

            int c = lireEntier(scanner, "Ton choix : ");
            if (c == 0) return null;
            if (c < 1 || c > player.getTeam().size()) {
                System.out.println("❌ Numéro invalide.");
                continue;
            }
            Monster picked = player.getTeam().get(c - 1);
            if (picked.isKo()) {
                System.out.println("❌ Ce monstre est KO.");
                continue;
            }
            return picked;
        }
    }

    private void afficherPhaseCombat(Monster a, Monster b) {
        System.out.println("--- Phase de combat ---");
        System.out.println(a);
        System.out.println("VS");
        System.out.println(b);
        System.out.println();
    }

    private Monster firstAlive() {
        return player.getTeam().stream().filter(m -> !m.isKo()).findFirst().orElse(null);
    }

    private int lireEntier(Scanner scanner, String msg) {
        while (true) {
            System.out.print(msg);
            String s = scanner.nextLine().trim();
            try { return Integer.parseInt(s); }
            catch (NumberFormatException e) { System.out.println("❌ Nombre attendu.\n"); }
        }
    }

    // dégâts = attaque (x2 si efficace)
    private int calculDegats(Monster atk, Monster def) {
        int base = atk.getAttack();
        if (isEffective(atk.getElement(), def.getElement())) return base * 2;
        return base;
    }

    private boolean isEffective(Element atk, Element def) {
        return (atk == Element.WATER && def == Element.FIRE)
                || (atk == Element.FIRE && def == Element.PLANT)
                || (atk == Element.PLANT && def == Element.WATER);
    }

    private Monster randomWild() {
        Random r = new Random();
        int type = r.nextInt(3);
        int maxHp = 80 + r.nextInt(71);
        int atk = 10 + r.nextInt(11);

        return switch (type) {
            case 0 -> new FireMonster("SauvageFeu", maxHp, maxHp, atk);
            case 1 -> new WaterMonster("SauvageEau", maxHp, maxHp, atk);
            default -> new PlantMonster("SauvagePlante", maxHp, maxHp, atk);
        };
    }
}
