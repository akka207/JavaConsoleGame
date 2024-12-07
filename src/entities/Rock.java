package entities;

public class Rock extends Entity {
    private static final String NAME = "Rock";

    public Rock(entities.Position position, String symbol) {
        super(position, symbol, NAME);
    }
}
