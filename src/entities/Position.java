package entities;

public class Position {
    public int x;
    public int y;

    public static final Position ZERO = new Position(0, 0);
    public static final Position RIGHT = new Position(1, 0);
    public static final Position LEFT = new Position(-1, 0);
    public static final Position UP = new Position(0, -1);
    public static final Position DOWN = new Position(0, 1);

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean Equals(Position p) {
        return x == p.x && y == p.y;
    }

    public double Distance(Position p) {
        return Math.sqrt(Math.pow(p.x - this.x, 2) + Math.pow(p.y - this.y, 2));
    }

    public static double Distance(Position p1, Position p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    public Position Add(Position p) {
        return new Position(this.x + p.x, this.y + p.y);
    }

    public void Set(Position p) {
        this.x = p.x;
        this.y = p.y;
    }
}
