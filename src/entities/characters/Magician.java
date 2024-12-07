package entities.characters;

import entities.Character;
import entities.Entity;
import entities.Position;
import game.ActionResultType;
import game.ActionType;
import game.ConsoleManager;
import game.Field;
import staticClasses.Colors;
import staticClasses.RandomRangesConfig;

public class Magician extends Character {
    private static final String NAME = "Magician";

    public Magician(entities.Position position, String symbol) {
        super(position, symbol, NAME, RandomRangesConfig.MAGICIAN_MAX_HP,
                RandomRangesConfig.MAGICIAN_DAMAGE_RANGE_MIN, RandomRangesConfig.MAGICIAN_DAMAGE_RANGE_MAX);
    }

    public ActionResultType Heal(Character character, int[] value) {
        actionable = false;
        int healedValue = (int) (Math.random()
                * (RandomRangesConfig.MAGICIAN_HEAL_RANGE_MAX - RandomRangesConfig.MAGICIAN_HEAL_RANGE_MIN + 1)
                + RandomRangesConfig.MAGICIAN_HEAL_RANGE_MIN);

        value[0] = character.IncreaseHealth(healedValue);
        return ActionResultType.Healed;
    }

    @Override
    public ActionResultType TryDamageOther(Position position, Field field, int[] damageValue) {
        damageValue[0] = (int) (Math.random() * (DAMAGE_RANGE_MAX - DAMAGE_RANGE_MIN + 1) + DAMAGE_RANGE_MIN);
        Entity entity = field.GetEntityByPosition(position);
        actionable = false;

        if (entity instanceof Character character) {
            character.ReduceOwnHealth(damageValue[0]);
            if (!character.IsAlive()) {
                return ActionResultType.Killed;
            }
            return ActionResultType.Damaged;
        } else if (entity == null) {
            return ActionResultType.InvalidDirection;
        } else {
            return ActionResultType.Missed;
        }
    }

    @Override
    public ActionType[] GetActionTypes() {
        return new ActionType[]{ActionType.Damage, ActionType.Heal, ActionType.Move};
    }

    @Override
    public String GetName() {
        return IsPoisoned ? Colors.Format(NAME, Colors.GREEN) : NAME;
    }
}
