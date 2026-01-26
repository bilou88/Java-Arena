package fr.javaarena.persistence;

import fr.javaarena.model.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class SaveManager {
    private static final String SAVE_FILE = "save.csv";

    // TEAM_NAME;<nom>
    // CREDITS;<credits>
    // MONSTER;<ELEMENT>;<NAME>;<HP>;<MAXHP>;<ATK>
    // ITEM;<NAME>;<QTY>
    public void save(Player player) {
        try (BufferedWriter w = Files.newBufferedWriter(Path.of(SAVE_FILE))) {
            w.write("TEAM_NAME;" + player.getTeamName()); w.newLine();
            w.write("CREDITS;" + player.getCredits()); w.newLine();

            for (Monster m : player.getTeam()) {
                w.write("MONSTER;" + m.getElement() + ";" + m.getName() + ";" + m.getHp() + ";" + m.getMaxHp() + ";" + m.getAttack());
                w.newLine();
            }

            for (Map.Entry<String, Integer> e : player.getInventory().entrySet()) {
                w.write("ITEM;" + e.getKey() + ";" + e.getValue());
                w.newLine();
            }
        } catch (Exception e) {
            System.out.println("❌ Erreur sauvegarde : " + e.getMessage());
        }
    }

    public Player load() {
        if (!Files.exists(Path.of(SAVE_FILE))) return null;

        try (BufferedReader r = Files.newBufferedReader(Path.of(SAVE_FILE))) {
            Player player = null;
            String line;

            while ((line = r.readLine()) != null) {
                String[] p = line.split(";");
                if (p.length == 0) continue;

                switch (p[0]) {
                    case "TEAM_NAME" -> player = new Player(p[1]);
                    case "CREDITS" -> { if (player != null) player.setCredits(Integer.parseInt(p[1])); }
                    case "MONSTER" -> {
                        if (player == null) break;
                        Element el = Element.valueOf(p[1]);
                        String name = p[2];
                        int hp = Integer.parseInt(p[3]);
                        int max = Integer.parseInt(p[4]);
                        int atk = Integer.parseInt(p[5]);

                        Monster m = switch (el) {
                            case FIRE -> new FireMonster(name, max, hp, atk);
                            case WATER -> new WaterMonster(name, max, hp, atk);
                            case PLANT -> new PlantMonster(name, max, hp, atk);
                        };
                        player.getTeam().add(m);
                    }
                    case "ITEM" -> { if (player != null) player.getInventory().put(p[1], Integer.parseInt(p[2])); }
                }
            }
            return player;

        } catch (Exception e) {
            System.out.println("❌ Erreur chargement : " + e.getMessage());
            return null;
        }
    }
}
