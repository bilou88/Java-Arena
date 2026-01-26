package src.com.javaArena.model;

public class MonstreEau extends Monstre {
    
    public MonstreEau(String nom, int pvMax, int puissanceAttaque) {
        super(nom, pvMax, puissanceAttaque);
    }
    
    public String getType() {
        return "Eau";
    }
    
    public int calculerDegats(Monstre cible) {
        int degats = this.getPuissanceAttaque();
        if (cible.getType().equals("Feu")) {
            degats *= 2;
        }
        return degats;
    }
}