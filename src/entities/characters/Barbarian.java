package entities.characters;

import entities.Character;
import entities.Entity;
import entities.Position;
import game.ActionResultType;
import game.ActionType;
import game.Field;
import staticClasses.Colors;
import staticClasses.RandomRangesConfig;

public class Barbarian extends Character {
    private static final String NAME = "Barbarian";

    public Barbarian(entities.Position position, String symbol) {
        super(position, symbol, NAME, RandomRangesConfig.BARBARIAN_MAX_HP,
                RandomRangesConfig.BARBARIAN_DAMAGE_RANGE_MIN, RandomRangesConfig.BARBARIAN_DAMAGE_RANGE_MAX);
    }

    @Override
    public ActionResultType TryDamageOther(Position position, Field field, int[] damageValue) {
        if (Position.Distance(position) <= 1) {
            damageValue[0] = (int) (Math.random() * (DAMAGE_RANGE_MAX - DAMAGE_RANGE_MIN + 1) + DAMAGE_RANGE_MIN);
            Entity entity = field.GetEntityByPosition(position);

            actionable = false;
            if (entity instanceof Character character) {
                character.ReduceOwnHealth(damageValue[0]);
                if (!character.IsAlive()) {
                    return ActionResultType.Killed;
                }
                return ActionResultType.Damaged;
            } else if(entity == null) {
                return ActionResultType.InvalidDirection;
            }
            else {
                return ActionResultType.Missed;
            }

        }
        return ActionResultType.Unsuccessful;
    }

    @Override
    public ActionType[] GetActionTypes() {
        return new ActionType[]{ActionType.Damage, ActionType.Move};
    }

    @Override
    public String GetName() {
        return IsPoisoned ? Colors.Format(NAME, Colors.GREEN) : NAME;
    }
}
