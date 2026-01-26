package src.com.javaArena.util;

import src.com.javaArena.model.Monstre;
import src.com.javaArena.model.MonstreFeu;
import src.com.javaArena.model.MonstreEau;
import src.com.javaArena.model.MonstrePlante;
import java.util.Random;

public class MonstreFactory {
    private static final String[] NOMS_FEU = {"Pyro", "Flamix", "Inferno", "Braise"};
    private static final String[] NOMS_EAU = {"Aqua", "Hydrox", "Vague", "Torrent"};
    private static final String[] NOMS_PLANTE = {"Vegeta", "Chloro", "Bourgeon", "Jungle"};
    
    private static final Random random = new Random();
    
    public static Monstre creerMonstresAleatoire() {
        int type = random.nextInt(3);
        
        switch (type) {
            case 0:
                return creerMonstreFeu();
            case 1:
                return creerMonstreEau();
            case 2:
                return creerMonstrePlante();
            default:
                return creerMonstreFeu();
        }
    }
    
    public static Monstre creerMonstreFeu() {
        String nom = NOMS_FEU[random.nextInt(NOMS_FEU.length)];
        int pvMax = 80 + random.nextInt(40);
        int puissance = 15 + random.nextInt(10);
        return new MonstreFeu(nom, pvMax, puissance);
    }
    
    public static Monstre creerMonstreEau() {
        String nom = NOMS_EAU[random.nextInt(NOMS_EAU.length)];
        int pvMax = 100 + random.nextInt(30);
        int puissance = 12 + random.nextInt(10);
        return new MonstreEau(nom, pvMax, puissance);
    }
    
    public static Monstre creerMonstrePlante() {
        String nom = NOMS_PLANTE[random.nextInt(NOMS_PLANTE.length)];
        int pvMax = 90 + random.nextInt(35);
        int puissance = 14 + random.nextInt(10);
        return new MonstrePlante(nom, pvMax, puissance);
    }
}