package fr.javaarena.model;

public class FireMonster extends Monster {
    public FireMonster(String name, int maxHp, int hp, int attack) { super(name, maxHp, hp, attack); }
    @Override public Element getElement() { return Element.FIRE; }
}
