package src.com.javaArena.model;

import src.com.javaArena.exception.MonstreKOException;
import src.com.javaArena.exception.CombatTermineException;
import src.com.javaArena.exception.CaptureImpossibleException;
import src.com.javaArena.exception.MonstreException;
import java.util.Random;

public class Combat {
    private Dresseur dresseur;
    private Monstre monstresauvage;
    private Monstre monstreActuel;
    private boolean combatTermine;
    
    public Combat(Dresseur dresseur, Monstre monstresauvage) {
        this.dresseur = dresseur;
        this.monstresauvage = monstresauvage;
        this.combatTermine = false;
        this.monstreActuel = dresseur.getMonstre(0);
    }
    
    public Dresseur getDresseur() {
        return dresseur;
    }
    
    public Monstre getMonstresauvage() {
        return monstresauvage;
    }
    
    public Monstre getMonstreActuel() {
        return monstreActuel;
    }
    
    public void setMonstreActuel(Monstre monstre) {
        this.monstreActuel = monstre;
    }
    
    public boolean isCombatTermine() {
        return combatTermine;
    }
    
    public void setCombatTermine(boolean termine) {
        this.combatTermine = termine;
    }
    
    public void attaquerMonstre() throws MonstreKOException, CombatTermineException {
        if (combatTermine) {
            throw new CombatTermineException("Le combat est déjà terminé");
        }
        if (monstreActuel == null || monstreActuel.estKO()) {
            throw new MonstreKOException("Le monstre actuel est KO");
        }
        if (monstresauvage.estKO()) {
            throw new MonstreKOException("Le monstre sauvage est déjà KO");
        }
        
        int degats = monstreActuel.calculerDegats(monstresauvage);
        monstresauvage.subir_degats(degats);
        
        if (monstresauvage.estKO()) {
            dresseur.ajouterCredits(10);
            combatTermine = true;
            return;
        }
        
        attaqueAdverse();
    }
    
    private void attaqueAdverse() {
        if (!monstreActuel.estKO()) {
            int degats = monstresauvage.calculerDegats(monstreActuel);
            monstreActuel.subir_degats(degats);
        }
    }
    
    public boolean capturerMonstre() throws CombatTermineException, CaptureImpossibleException {
        if (combatTermine) {
            throw new CombatTermineException("Le combat est déjà terminé");
        }
        
        double pourcentageVie = (double) monstresauvage.getPvActuels() / monstresauvage.getPvMax();
        if (pourcentageVie > 0.3) {
            throw new CaptureImpossibleException("Le monstre a trop de PV pour être capturé");
        }
        
        if (!dresseur.utiliserObjet("Pokéball", 1)) {
            return false;
        }
        
        Random random = new Random();
        if (random.nextDouble() < 0.6) {
            combatTermine = true;
            return true;
        }
        
        attaqueAdverse();
        return false;
    }
    
    public void utiliserPotion(int quantitePotion, Monstre monstre) throws MonstreException {
        if (monstre.getPvActuels() == monstre.getPvMax()) {
            throw new MonstreException("Le monstre a déjà tous ses PV");
        }
        
        monstre.soigner(quantitePotion * 30);
        attaqueAdverse();
    }
    
    public void changerMonstre(Monstre nouveauMonstre) throws MonstreKOException {
        if (nouveauMonstre.estKO()) {
            throw new MonstreKOException("Impossible de changer vers un monstre KO");
        }
        monstreActuel = nouveauMonstre;
        attaqueAdverse();
    }
}