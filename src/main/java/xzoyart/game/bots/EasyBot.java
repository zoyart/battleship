package xzoyart.game.bots;

import xzoyart.game.Field;
import xzoyart.game.Ship;
import xzoyart.game.coordinate.Coordinate;

import java.util.List;

public class EasyBot extends Bot {

    /**
     * Метод отвечает за выстрел бота по вражескому игровому полю.
     * @param enemyField игровое поле противника.
     * @return результат выстрела.
     */
    public String shot(Field enemyField) {
        List<Coordinate> emptyCoordinates = enemyField.getEmptyCoordinates();
        int minValue = 0;
        int maxValue = emptyCoordinates.size() - 1;
        int randomCoordinate = minValue + (int) (Math.random() * (maxValue - minValue + 1));

        Coordinate shotCoordinate = emptyCoordinates.get(randomCoordinate);

        enemyField.addAllShotsInField(shotCoordinate);

        boolean isHit = false;
        String shotResult = "";

        // Перебор всех координат, всех кораблей противника
        List<Ship> enemyShips = enemyField.getShips();
        for (Ship enemyShip : enemyShips) {
            // При попадании в палубу корабля
            if (enemyShip.containsCoordinate(shotCoordinate)) {
                enemyShip.deckDestroy(shotCoordinate);
                enemyField.addHit(shotCoordinate);
                isHit = true;

                if (enemyShip.getAfloatDecksCoordinates().isEmpty()) {
                    shotResult = "корабль игрока потоплен!";

                    // Добавление "промахов" вокруг уничтоженного корабля.
                    List<Coordinate> allShotsCoordinates = enemyField.addMissAroundShip(enemyShip);
                    for (Coordinate coordinate : allShotsCoordinates) {
                        enemyField.addAllShotsInField(coordinate);
                    }
                } else {
                    shotResult = "попадание по кораблю игрока!";
                }
            }
        }

        if (!isHit) {
            enemyField.addMiss(shotCoordinate);
            shotResult = "промах.";
        }

        return shotResult;
    }
}
