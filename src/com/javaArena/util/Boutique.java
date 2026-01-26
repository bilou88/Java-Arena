package src.com.javaArena.util;

import src.com.javaArena.model.Dresseur;
import java.util.HashMap;
import java.util.Map;

public class Boutique {
    private static final Map<String, Integer> PRIX = new HashMap<>();
    
    static {
        PRIX.put("Potion", 10);
        PRIX.put("Potion Ressurection", 50);
        PRIX.put("Pokéball", 15);
    }
    
    public static void afficherBoutique() {
        System.out.println("\n=== BOUTIQUE ===");
        for (Map.Entry<String, Integer> entry : PRIX.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue() + " crédits");
        }
    }
    
    public static boolean acheter(Dresseur dresseur, String objet, int quantite) {
        if (!PRIX.containsKey(objet)) {
            System.out.println("Objet inexistant");
            return false;
        }
        
        int cout = PRIX.get(objet) * quantite;
        
        if (!dresseur.depenser_credits(cout)) {
            System.out.println("Crédits insuffisants");
            return false;
        }
        
        dresseur.ajouterObjet(objet, quantite);
        System.out.println("Achat réussi !");
        return true;
    }
}