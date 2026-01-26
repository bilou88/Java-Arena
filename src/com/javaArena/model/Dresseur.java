package src.com.javaArena.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dresseur {
    private String nomEquipe;
    private List<Monstre> equipe;
    private Map<String, Integer> inventaire;
    private int credits;
    
    public Dresseur(String nomEquipe) {
        this.nomEquipe = nomEquipe;
        this.equipe = new ArrayList<>();
        this.inventaire = new HashMap<>();
        this.credits = 0;
    }
    
    public String getNomEquipe() {
        return nomEquipe;
    }
    
    public void setNomEquipe(String nomEquipe) {
        this.nomEquipe = nomEquipe;
    }
    
    public List<Monstre> getEquipe() {
        return equipe;
    }
    
    public void ajouterMonstre(Monstre monstre) {
        if (equipe.size() < 3) {
            equipe.add(monstre);
        }
    }
    
    public Monstre getMonstre(int index) {
        if (index >= 0 && index < equipe.size()) {
            return equipe.get(index);
        }
        return null;
    }
    
    public Map<String, Integer> getInventaire() {
        return inventaire;
    }
    
    public void ajouterObjet(String nomObjet, int quantite) {
        inventaire.put(nomObjet, inventaire.getOrDefault(nomObjet, 0) + quantite);
    }
    
    public boolean utiliserObjet(String nomObjet, int quantite) {
        if (inventaire.containsKey(nomObjet) && inventaire.get(nomObjet) >= quantite) {
            int nouvelle_quantite = inventaire.get(nomObjet) - quantite;
            if (nouvelle_quantite == 0) {
                inventaire.remove(nomObjet);
            } else {
                inventaire.put(nomObjet, nouvelle_quantite);
            }
            return true;
        }
        return false;
    }
    
    public int getCredits() {
        return credits;
    }
    
    public void setCredits(int credits) {
        this.credits = Math.max(0, credits);
    }
    
    public void ajouterCredits(int quantite) {
        credits += quantite;
    }
    
    public boolean depenser_credits(int quantite) {
        if (credits >= quantite) {
            credits -= quantite;
            return true;
        }
        return false;
    }
    
    public int compterMonstresVivants() {
        int count = 0;
        for (Monstre monstre : equipe) {
            if (!monstre.estKO()) {
                count++;
            }
        }
        return count;
    }
}