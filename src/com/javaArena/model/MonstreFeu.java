package src.com.javaArena.model;

public class MonstreFeu extends Monstre {
    
    public MonstreFeu(String nom, int pvMax, int puissanceAttaque) {
        super(nom, pvMax, puissanceAttaque);
    }
    
    public String getType() {
        return "Feu";
    }
    
    public int calculerDegats(Monstre cible) {
        int degats = this.getPuissanceAttaque();
        if (cible.getType().equals("Plante")) {
            degats *= 2;
        }
        return degats;
    }
}