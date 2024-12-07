package game;

import entities.Character;
import staticClasses.Colors;

public class Player {
    public final String Name;
    public Character[] characters;

    private final String color;


    public Player(String name, Character[] characters, String color) {
        Name = name;
        this.characters = characters;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return Colors.Format(Name, color);
    }

    public int CountActionableCharacters() {
        int count = 0;
        for (Character character : characters) {
            if(character.IsActionable()){
                count++;
            }
        }
        return count;
    }
}
