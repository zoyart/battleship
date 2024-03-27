package xzoyart.game;

import org.apache.commons.lang3.SerializationUtils;
import xzoyart.game.coordinate.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private List<Deck> decks = new ArrayList<>();

    public Ship(int deckCount) {
        for (int i = 0; i < deckCount; i++) {
            this.decks.add(new Deck());
        }
    }

    public int getDecksCount() {
        return this.decks.size();
    }

    public List<Coordinate> getAllDeckCoordinates() {
        List<Coordinate> coordinates = new ArrayList<>();

        for (Deck deck : decks) {
            coordinates.add(deck.getCoordinate());
        }

        return coordinates;
    }

    /**
     * @return возвращает все координаты палуб корабля, которые на плаву.
     */
    public List<Coordinate> getAfloatDecksCoordinates() {
        List<Coordinate> coordinates = new ArrayList<>();
        for (Deck deck : this.decks) {
            if (deck.getAfloat()) {
                coordinates.add(deck.getCoordinate());
            }
        }
        return coordinates;
    }

    /**
     * @return возвращает все координаты потопленных палуб у корабля.
     */
    public List<Coordinate> getDestroyedDecksCoordinates() {
        List<Coordinate> coordinates = new ArrayList<>();
        for (Deck deck : this.decks) {
            if ( ! deck.getAfloat()) {
                coordinates.add(deck.getCoordinate());
            }
        }
        return coordinates;
    }

    public List<Deck> getDecks() {
        return this.decks;
    }

    public boolean isAfloat() {
        List<Coordinate> afloatDeckCoordinates = getAfloatDecksCoordinates();

        return afloatDeckCoordinates.size() > 0;
    }

    /**
     * Метод отвечает за изменение параметра isAfloat на "false", объекта Deck, по координате.
     * @param destroyCoordinate координаты Deck, которую необходимо поментить как уничтоженную.
     */
    public void deckDestroy(Coordinate destroyCoordinate) {
        List<Deck> decks = getDecks();

        for (Deck deck : decks) {
            Coordinate coordinate = deck.getCoordinate();
            int horizontal = coordinate.getHorizontal();
            int vertical = coordinate.getVertical();

            if (horizontal == destroyCoordinate.getHorizontal() && vertical == destroyCoordinate.getVertical()) {
                deck.setAfloat(false);
            }
        }
    }

    /**
     * Метод проверяет есть ли данная координата у текущего корабля.
     * @param searchCoordinate координата, которую необходимо найти у корабля
     * @return boolean
     */
    public boolean containsCoordinate(Coordinate searchCoordinate) {
        List<Coordinate> allDeckCoordinates = getAllDeckCoordinates();

        for (Coordinate coordinate : allDeckCoordinates) {
            if (coordinate.getHorizontal() == searchCoordinate.getHorizontal() &&
                    coordinate.getVertical() == searchCoordinate.getVertical())
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Метод отвечает за добавление координат всем палубам корабля.
     * @param startCoordinate с какой координаты начинать поставновку
     */
    public void addAllCoordinates(Coordinate startCoordinate) {
        if (startCoordinate.getVector().equalsIgnoreCase("h")) {
            for (Deck deck : this.decks) {
                Coordinate newCoordinate = SerializationUtils.clone(startCoordinate);
                deck.setCoordinate(newCoordinate);
                startCoordinate.horizontalIncrement();
            }
        }

        if (startCoordinate.getVector().equalsIgnoreCase("v")) {
            for (Deck deck : this.decks) {
                Coordinate newCoordinate = SerializationUtils.clone(startCoordinate);
                deck.setCoordinate(newCoordinate);
                startCoordinate.verticalIncrement();
            }
        }
    }
}
