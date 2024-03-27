package xzoyart.game;

import xzoyart.game.coordinate.Coordinate;

public class Deck {
    private boolean isAfloat = true;
    private Coordinate coordinate;

    public boolean getAfloat() {
        return this.isAfloat;
    }

    public void setAfloat(boolean isAfloat) {
        this.isAfloat = isAfloat;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return this.coordinate;
    }
}
