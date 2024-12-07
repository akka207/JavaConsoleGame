package entities;

import game.ConsoleManager;
import staticClasses.RandomRangesConfig;

import java.io.Console;


public class Poison extends Entity {
    private static final String NAME = "Poison";

    public Poison(entities.Position position, String symbol) {
        super(position, symbol, NAME, 3);
    }

    public static int GenerateDamageValue() {
        int damage = RandomRangesConfig.POISON_DAMAGE_RANGE_MIN +
                (int) (Math.random() * (RandomRangesConfig.POISON_DAMAGE_RANGE_MAX - RandomRangesConfig.POISON_DAMAGE_RANGE_MIN));
        ConsoleManager.Instance.SubTitle("Poison takes " + damage + " hp");
        return damage;
    }
}
