package fr.javaarena.model;

public class PlantMonster extends Monster {
    public PlantMonster(String name, int maxHp, int hp, int attack) { super(name, maxHp, hp, attack); }
    @Override public Element getElement() { return Element.PLANT; }
}
