package game;

import entities.Character;
import entities.Entity;
import entities.Position;

public class ActionParams {
    public Character Character = null;
    public ActionType BattleAction;
    public Object Value;
    public Position Destination;

    public ActionParams(){

    }

    public ActionParams(Character character, ActionType battleAction, Object value, Position destination) {
        Character = character;
        BattleAction = battleAction;
        Value = value;
        Destination = destination;
    }
}
