package fr.javaarena.model;

import java.util.*;

public class Player {
    private final String teamName;
    private final List<Monster> team = new ArrayList<>();
    private final Map<String, Integer> inventory = new HashMap<>();
    private int credits = 0;

    public Player(String teamName) { this.teamName = teamName; }

    public String getTeamName() { return teamName; }
    public List<Monster> getTeam() { return team; }
    public Map<String, Integer> getInventory() { return inventory; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public void printTeamStatus() {
        System.out.println("\n--- Équipe : " + teamName + " ---");
        for (int i = 0; i < team.size(); i++) {
            System.out.println((i + 1) + ") " + team.get(i));
        }
    }

    public static Player newPlayerWithRandomTeam(String teamName) {
        Player p = new Player(teamName);
        Random r = new Random();

        for (int i = 0; i < 3; i++) {
            int type = r.nextInt(3);
            int maxHp = 80 + r.nextInt(71);   // 80..150
            int atk = 10 + r.nextInt(11);     // 10..20

            Monster m = switch (type) {
                case 0 -> new FireMonster("MonstreFeu" + (i + 1), maxHp, maxHp, atk);
                case 1 -> new WaterMonster("MonstreEau" + (i + 1), maxHp, maxHp, atk);
                default -> new PlantMonster("MonstrePlante" + (i + 1), maxHp, maxHp, atk);
            };
            p.team.add(m);
        }

        p.inventory.put("PotionSoin", 2);
        p.inventory.put("PotionResurrection", 1);
        p.inventory.put("OutilCapture", 1);

        return p;
    }
}
