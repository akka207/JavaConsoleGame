package entities;

import game.ActionResultType;
import game.ActionType;
import game.Field;
import staticClasses.Colors;

public abstract class Character extends Entity {
    public final int DAMAGE_RANGE_MIN;
    public final int DAMAGE_RANGE_MAX;

    public boolean IsPoisoned = false;

    private final int MAX_HEALTH;
    private int health;

    public Character(entities.Position position, String symbol, String name,
                     int health, int damageRangeMin, int damageRangeMax) {
        super(position, symbol, name);
        this.health = health;
        DAMAGE_RANGE_MIN = damageRangeMin;
        DAMAGE_RANGE_MAX = damageRangeMax;
        MAX_HEALTH = health;
    }

    public abstract ActionResultType TryDamageOther(Position position, Field field, int[] damageValue);

    public int getHealth() {
        return health;
    }

    public boolean IsAlive() {
        return health > 0;
    }

    public int IncreaseHealth(int value) {
        if (health + value < MAX_HEALTH) {
            health += value;
            return value;
        } else {
            int healedValue = MAX_HEALTH - health;
            health = MAX_HEALTH;
            return healedValue;
        }
    }

    public void ReduceOwnHealth(int amount) {
        health -= amount;
    }

    public int GenerateDamageValue() {
        return DAMAGE_RANGE_MIN + (int) (Math.random() * (DAMAGE_RANGE_MAX - DAMAGE_RANGE_MIN));
    }

    public abstract ActionType[] GetActionTypes();

    public abstract String GetName();

    @Override
    public void Tick() {
        super.Tick();
        if (IsPoisoned) {
            ReduceOwnHealth(Poison.GenerateDamageValue());
        }
    }
}
