package xzoyart.game.bots;

import org.apache.commons.lang3.SerializationUtils;
import xzoyart.game.Field;
import xzoyart.game.Settings;
import xzoyart.game.Ship;
import xzoyart.game.coordinate.Coordinate;
import xzoyart.utils.Validator;

import java.util.ArrayList;
import java.util.List;

public class Bot {
    protected Field field = new Field();

    public Field getField() {
        return this.field;
    }

    public void shipPlacement() {
        List<Ship> ships = new ArrayList<>(List.of(
            new Ship(4),
            new Ship(3),
            new Ship(3),
            new Ship(2),
            new Ship(2),
            new Ship(2),
            new Ship(1),
            new Ship(1),
            new Ship(1),
            new Ship(1)
        ));
        Coordinate coordinate = new Coordinate();

        for (Ship ship : ships) {
            boolean isValidated = false;
            while ( !isValidated ) {
                try {
                    int minValue = 1;
                    int maxValue = Settings.getFieldSize();
                    int randomHorizontal = minValue + (int) (Math.random() * (maxValue - minValue + 1));
                    int randomVertical = minValue + (int) (Math.random() * (maxValue - minValue + 1));
                    String randomVector = randomHorizontal > randomVertical ? "v" : "h";

                    coordinate.setHorizontal(randomHorizontal);
                    coordinate.setVertical(randomVertical);
                    coordinate.setVector(randomVector);

                    int deckCount = ship.getDecksCount();
                    Validator.shipLeavingField(coordinate, deckCount);

                    /**
                     * Формирование всех потенциальных координат для постановки корабля
                     */
                    Coordinate verifiableCoordinate = SerializationUtils.clone(coordinate);
                    List<Coordinate> shipCoordinates = new ArrayList<>();
                    for (int i = 0; i < deckCount; i++) {
                        shipCoordinates.add(SerializationUtils.clone(verifiableCoordinate));
                        if (verifiableCoordinate.getVector().equalsIgnoreCase("h")) {
                            verifiableCoordinate.horizontalIncrement();
                        } else {
                            verifiableCoordinate.verticalIncrement();
                        }
                    }

                    /**
                     * Происходит проверка на то, чтобы коодинаты текущего корабля, не касались других на игровом поле.
                     */
                    for (Coordinate checkCoordinate : shipCoordinates) {
                        Validator.isEmptyAroundCell(checkCoordinate, this.field);
                    }

                    isValidated = true;
                } catch (Exception e) {}
            }
            ship.addAllCoordinates(coordinate);
            field.addShip(ship);
        }
    }

    public String shot(Field enemyField) {
        return "";
    }
}
