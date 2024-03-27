package xzoyart.game.bots;

import xzoyart.game.Field;
import xzoyart.game.Ship;
import xzoyart.game.coordinate.Coordinate;

import java.util.List;

public class HardBot extends Bot {
    private Ship target = null;

    /**
     * Метод отвечает за выстрел бота по вражескому игровому полю.
     * @param enemyField игровое поле противника.
     * @return
     */
    public String shot(Field enemyField) {
        Coordinate shotCoordinate = null;

        /**
         * Если есть какой-то корабль в target, то будет произведена атака по нему, иначе по рандомной свободной клетке.
         */
        if (target != null) {
            List<Coordinate> afloatDeckCoordinates = target.getAfloatDecksCoordinates();

            shotCoordinate = afloatDeckCoordinates.get(0);
        } else {
            List<Coordinate> emptyCoordinates = enemyField.getEmptyCoordinates();
            int minValue = 0;
            int maxValue = emptyCoordinates.size() - 1;
            int randomCoordinate = minValue + (int) (Math.random() * (maxValue - minValue + 1));

            shotCoordinate = emptyCoordinates.get(randomCoordinate);
        }

        // Добавление выстрела в пул
        enemyField.addAllShotsInField(shotCoordinate);

        /**
         * Будет ли бот атаковать текущий корабль при следующей атаке.
         */
        boolean isTarget = false;

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
                isTarget = true;

                // При уничтожении корабля
                if (enemyShip.getAfloatDecksCoordinates().isEmpty()) {
                    shotResult = "корабль игрока потоплен!";

                    // Сброс цели при её уничтожении
                    target = null;
                    isTarget = false;

                    // Добавление "промахов" вокруг уничтоженного корабля.
                    List<Coordinate> allShotsCoordinates = enemyField.addMissAroundShip(enemyShip);
                    for (Coordinate coordinate : allShotsCoordinates) {
                        enemyField.addAllShotsInField(coordinate);
                    }
                } else {
                    shotResult = "попадание по кораблю игрока!";
                }

                // Добавление вражеского корабля в цель следующей атаки
                if (isTarget) {
                    this.target = enemyShip;
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
