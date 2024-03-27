package xzoyart.game;

import org.apache.commons.lang3.SerializationUtils;
import xzoyart.game.coordinate.Coordinate;
import xzoyart.utils.Input;
import xzoyart.utils.Validator;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private Field field = new Field();

    public Field getField() {
        return this.field;
    }

    /**
     * Метод:
     * 1. Принимает у пользователя ввод;
     * 2. валидирует данные;
     * 3. присваивает координаты всем кораблям;
     * 4. выводит на экран игровое поле после каждой итерации.
     */
    public void shipPlacement() {
        String userInput = "";
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
                    userInput = Input.userInput();
                    Validator.placementCoordinatesValidate(userInput);

                    String[] args = userInput.split(" ");
                    coordinate.create(args[0], args[1], args[2]);

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
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            ship.addAllCoordinates(coordinate);
            field.addShip(ship);
            this.field.print();
        }
    }

    public Coordinate inputShotCoordinates(Field enemyField) {
        String userInput = "";
        boolean check = true;
        Coordinate coordinate = new Coordinate();

        // Валидация на корректный ввод координат для постановки корабля
        while (check) {
            try {
                userInput = Input.userInput("Player shooting!");

                // * Проверка на правильность написания координат
                Validator.shotCoordinatesValidate(userInput, this.field);

                // Создание координат из ввода игрока
                String[] args = userInput.split(" ");
                coordinate.create(args[0], args[1]);

                // Проверка на то, чтобы координаты попадания были уникальные
                for (Coordinate shotCoordinate : enemyField.getAllShotsInField()) {
                    if (shotCoordinate.getHorizontal() == coordinate.getHorizontal() &&
                            shotCoordinate.getVertical() == coordinate.getVertical()
                    ) {
                        throw new Exception("Вы уже стреляли в эту область.");
                    }
                }

                check = false;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return coordinate;
    }

    /**
     * Метод отвечает за выстрел игрока по вражескому игровому полю.
     * @param enemyField игровое поле противника.
     * @return
     */
    public String shot(Field enemyField) {
        Coordinate shotCoordinate = inputShotCoordinates(enemyField);
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
                    shotResult = "вражеский корабль потоплен!";

                    // Добавление "промахов" вокруг уничтоженного корабля.
                    List<Coordinate> allShotsCoordinates = enemyField.addMissAroundShip(enemyShip);
                    for (Coordinate coordinate : allShotsCoordinates) {
                        enemyField.addAllShotsInField(coordinate);
                    }
                } else {
                    shotResult = "попадание по вражескому кораблю!";
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

