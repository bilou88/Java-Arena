package fr.javaarena.model;

public abstract class Monster {
    private final String name;
    private final int maxHp;
    private int hp;
    private final int attack;

    protected Monster(String name, int maxHp, int hp, int attack) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = hp;
        this.attack = attack;
    }

    public abstract Element getElement();

    public String getName() { return name; }
    public int getMaxHp() { return maxHp; }
    public int getHp() { return hp; }
    public int getAttack() { return attack; }

    public boolean isKo() { return hp <= 0; }

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, maxHp));
    }

    @Override
    public String toString() {
        return name + " (" + getElement() + ") : " + hp + "/" + maxHp + " PV" + (isKo() ? " (KO)" : "");
    }
}
