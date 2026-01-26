package fr.javaarena.model;

public class WaterMonster extends Monster {
    public WaterMonster(String name, int maxHp, int hp, int attack) { super(name, maxHp, hp, attack); }
    @Override public Element getElement() { return Element.WATER; }
}
