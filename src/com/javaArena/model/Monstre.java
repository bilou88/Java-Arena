package src.com.javaArena.model;

public abstract class Monstre {
    private String nom;
    private int pvActuels;
    private int pvMax;
    private int puissanceAttaque;
    
    public Monstre(String nom, int pvMax, int puissanceAttaque) {
        this.nom = nom;
        this.pvMax = pvMax;
        this.pvActuels = pvMax;
        this.puissanceAttaque = puissanceAttaque;
    }
    
    public abstract String getType();
    
    public abstract int calculerDegats(Monstre cible);
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public int getPvActuels() {
        return pvActuels;
    }
    
    public void setPvActuels(int pvActuels) {
        this.pvActuels = Math.max(0, Math.min(pvActuels, pvMax));
    }
    
    public int getPvMax() {
        return pvMax;
    }
    
    public int getPuissanceAttaque() {
        return puissanceAttaque;
    }
    
    public void setPuissanceAttaque(int puissanceAttaque) {
        this.puissanceAttaque = puissanceAttaque;
    }
    
    public boolean estKO() {
        return pvActuels <= 0;
    }
    
    public void soigner(int quantite) {
        setPvActuels(pvActuels + quantite);
    }
    
    public void subir_degats(int degats) {
        setPvActuels(pvActuels - degats);
    }
}