package src.com.javaArena.model;

public class MonstrePlante extends Monstre {
    
    public MonstrePlante(String nom, int pvMax, int puissanceAttaque) {
        super(nom, pvMax, puissanceAttaque);
    }
    
    public String getType() {
        return "Plante";
    }
    
    public int calculerDegats(Monstre cible) {
        int degats = this.getPuissanceAttaque();
        if (cible.getType().equals("Eau")) {
            degats *= 2;
        }
        return degats;
    }
}