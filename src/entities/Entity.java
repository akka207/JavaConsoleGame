package entities;

import staticClasses.Colors;

public abstract class Entity {
    public Position Position;

    private final String name;
    private String symbol;

    public final int MAX_LIFETIME;

    protected int lifeTime = 0;
    protected boolean actionable = true;

    public Entity(Position position, String symbol, String name) {
        this.Position = position;
        this.symbol = symbol;
        this.name = name;
        this.MAX_LIFETIME = -1;
    }

    public Entity(Position position, String symbol, String name, int maxLifeTime) {
        this.Position = position;
        this.symbol = symbol;
        this.name = name;
        this.MAX_LIFETIME = maxLifeTime;
    }

    public String GetName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean IsActionable() {
        return actionable;
    }

    public void MakeActionable() {
        actionable = true;
    }

    public void DoAction() {
        actionable = false;
    }

    public void setSymbolColor(String color) {
        Colors.Reformat(symbol);
        symbol = Colors.Format(symbol, color);
    }

    public void Tick() {
        lifeTime++;
    }

    public boolean CheckLifeTime() {
        return MAX_LIFETIME == -1 || lifeTime < MAX_LIFETIME;
    }
}
