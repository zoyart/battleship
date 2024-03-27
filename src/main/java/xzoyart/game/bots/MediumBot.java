package xzoyart.game.bots;

import xzoyart.game.Field;
import xzoyart.game.Ship;
import xzoyart.game.coordinate.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class MediumBot extends Bot {
    private List<Coordinate> possibleShotCoordinate = new ArrayList<>();
    private Coordinate lastShotCoordinate = null;
    private boolean isLastShotHit = false;

    /**
     * Метод отвечает за выстрел бота по вражескому игровому полю.
     * @param enemyField игровое поле противника.
     * @return
     */
    public String shot(Field enemyField) {
        Coordinate shotCoordinate = selectingCoordinateForShot(enemyField);

        // Перебор всех координат, всех кораблей противника
        enemyField.addAllShotsInField(shotCoordinate);
        List<Ship> enemyShips = enemyField.getShips();

        boolean isHit = false;
        String shotResult = "";
        for (Ship enemyShip : enemyShips) {
            // При попадании в палубу корабля
            if (enemyShip.containsCoordinate(shotCoordinate)) {
                enemyShip.deckDestroy(shotCoordinate);
                enemyField.addHit(shotCoordinate);
                isHit = true;

                // Добавляем последнему выстрелу такой же вектор как и у корабля
                shotCoordinate.setVector(enemyShip.getAllDeckCoordinates().getFirst().getVector());
                this.lastShotCoordinate = shotCoordinate;
                this.isLastShotHit = true;

                // При уничтожении корабля
                if (enemyShip.getAfloatDecksCoordinates().isEmpty()) {
                    shotResult = "корабль игрока потоплен!";

                    this.possibleShotCoordinate = new ArrayList<>();
                    this.lastShotCoordinate = null;
                    this.isLastShotHit = false;

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
            this.isLastShotHit = false;
            shotResult = "промах.";
        }

        return shotResult;
    }

    /**
     * Метод генерирует новые координаты для выстрела на основании старых, имитирует стандартное поведение
     * игрока при попытке уничтожения корабля.
     * @param enemyField вражеское поле
     * @return координаты для следующего выстрела
     */
    public Coordinate selectingCoordinateForShot(Field enemyField) {
        Coordinate shotCoordinate = null;

        // Если прошлый выстрел не попал по кораблю
        if (this.lastShotCoordinate == null) {
            List<Coordinate> emptyCoordinates = enemyField.getEmptyCoordinates();
            int minValue = 0;
            int maxValue = emptyCoordinates.size() - 1;
            int randomCoordinate = minValue + (int) (Math.random() * (maxValue - minValue + 1));
            shotCoordinate = emptyCoordinates.get(randomCoordinate);
        }

        // Если нет попадания в прошлом ходу, пропустить
        if (lastShotCoordinate != null) {
            // Если есть сформированные возможные координаты для удара - далее, иначе сформировать и взять первую.
            if (!possibleShotCoordinate.isEmpty()) {
                if (isLastShotHit) {
                    possibleShotCoordinate.removeFirst();

                    // При втором выстреле удаляем ненужные координаты из пула (с другим вектором)
                    if (lastShotCoordinate.getVector().equalsIgnoreCase("h")) {
                        for (int i = 0; i < possibleShotCoordinate.size(); i ++) {
                            if (possibleShotCoordinate.get(i).getVector().equalsIgnoreCase("v")) {
                                possibleShotCoordinate.remove(i);
                                i--;
                            }
                        }
                    }
                    if (lastShotCoordinate.getVector().equalsIgnoreCase("v")) {
                        for (int i = 0; i < possibleShotCoordinate.size(); i ++) {
                            if (possibleShotCoordinate.get(i).getVector().equalsIgnoreCase("h")) {
                                possibleShotCoordinate.remove(i);
                                i--;
                            }
                        }
                    }

                    // Добавление новых координат при втором и более попадании.
                    // В пул заносятся координаты по одной из векторов
                    int horizontal = lastShotCoordinate.getHorizontal();
                    int vertical = lastShotCoordinate.getVertical();

                    if (lastShotCoordinate.getVector().equalsIgnoreCase("h")) {
                        List<Coordinate> horizontalCoordinates = new ArrayList<>();
                        horizontalCoordinates.add(new Coordinate(horizontal + 1, vertical, "h"));
                        horizontalCoordinates.add(new Coordinate(horizontal - 1, vertical, "h"));

                        for (Coordinate coordinate : horizontalCoordinates) {
                            if (enemyField.containsDestroyedDecksCoordinate(coordinate)) continue;
                            if (coordinate.getHorizontal() > 9 || coordinate.getHorizontal() < 0) continue;
                            if ( !enemyField.containsEmptyCoordinates(coordinate) ) continue;

                            possibleShotCoordinate.add(coordinate);
                        }
                    } else {
                        List<Coordinate> verticalCoordinates = new ArrayList<>();
                        verticalCoordinates.add(new Coordinate(horizontal, vertical - 1, "v"));
                        verticalCoordinates.add(new Coordinate(horizontal, vertical + 1, "v"));

                        for (Coordinate coordinate : verticalCoordinates) {
                            if (enemyField.containsDestroyedDecksCoordinate(coordinate)) continue;
                            if (coordinate.getVertical() > 9 || coordinate.getVertical() < 0) continue;
                            if ( !enemyField.containsEmptyCoordinates(coordinate) ) continue;

                            possibleShotCoordinate.add(coordinate);
                        }
                    }

                    // Взятие следующей координаты из пула
                    shotCoordinate = possibleShotCoordinate.getFirst();
                } else {
                    // Удаление координаты, по которой не оказалось корабля
                    possibleShotCoordinate.removeFirst();

                    // Взятие следующей координаты из пула
                    shotCoordinate = possibleShotCoordinate.getFirst();
                }
            } else {

                // Создание возможных координат
                int horizontal = lastShotCoordinate.getHorizontal();
                int vertical = lastShotCoordinate.getVertical();

                List<Coordinate> possibleCoordinates = new ArrayList<>();
                possibleCoordinates.add(new Coordinate(horizontal, vertical - 1, "v"));
                possibleCoordinates.add(new Coordinate(horizontal, vertical + 1, "v"));
                possibleCoordinates.add(new Coordinate(horizontal - 1, vertical, "h"));
                possibleCoordinates.add(new Coordinate(horizontal + 1, vertical, "h"));

                // Проверка на выход координаты за пределы поля
                for (Coordinate coordinate : possibleCoordinates) {
                    if (coordinate.getHorizontal() > 9 && coordinate.getHorizontal() < 0) continue;
                    if (coordinate.getVertical() > 9 && coordinate.getVertical() < 0) continue;
                    if ( !enemyField.containsEmptyCoordinates(coordinate) ) continue;

                    possibleShotCoordinate.add(coordinate);
                }

                // Взятие следующей координаты из пула
                shotCoordinate = possibleShotCoordinate.getFirst();
            }
        }

        return shotCoordinate;
    }
}
