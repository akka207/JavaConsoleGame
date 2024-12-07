package entities.characters;

import entities.Character;
import entities.Entity;
import entities.Poison;
import entities.Position;
import game.ActionResultType;
import game.ActionType;
import game.Field;
import staticClasses.Colors;
import staticClasses.RandomRangesConfig;
import staticClasses.Symbols;

public class Witch extends Character {
    private static final String NAME = "Witch";

    public Witch(entities.Position position, String symbol) {
        super(position, symbol, NAME, RandomRangesConfig.WITCH_MAX_HP,
                RandomRangesConfig.WITCH_DAMAGE_RANGE_MIN, RandomRangesConfig.WITCH_DAMAGE_RANGE_MAX);
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
            field.SetEntity(new Poison(position, Symbols.POISON), Colors.GREEN);
            return ActionResultType.Damaged;
        } else if(entity == null) {
            field.SetEntity(new Poison(position, Symbols.POISON), Colors.GREEN);
            return ActionResultType.Summoned;
        }
        else {
            return ActionResultType.Missed;
        }
    }

    @Override
    public ActionType[] GetActionTypes() {
        return new ActionType[]{ActionType.Damage, ActionType.Move};
    }

    @Override
    public String GetName() {
        return NAME;
    }
}
