package xzoyart.game;

import xzoyart.game.coordinate.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class Field {
    // Размеры игрового поля
    private final int size = Settings.getFieldSize();

    // Все выстрелы по текущему полю
    private List<Coordinate> allShotsInField = new ArrayList<>();

    // Все промахи по текущему полю
    private List<Coordinate> allMistakes = new ArrayList<>();

    // Все свободные координаты для выстрела или постановки корабля
    private List<Coordinate> emptyCoordinates = new ArrayList<>();

    // Все корабли
    private List<Ship> ships = new ArrayList<>();

    public Field() {
        // Формирование всех координат для поля
        for (int i = 1; i <= Settings.getFieldSize(); i++) {
            for (int k = 1; k <= Settings.getFieldSize(); k++) {
                Coordinate coordinate = new Coordinate();
                coordinate.setHorizontal(i);
                coordinate.setVertical(k);

                emptyCoordinates.add(coordinate);
            }
        }
    }

    public int getSize() {
        return this.size;
    }

    public List<Ship> getShips() {
        return this.ships;
    }

    /**
     * Метод считает количесвто кораблей "на плаву".
     * @return возвращает количество "живых" кораблей.
     */
    public int getShipsCount() {
        int count = 0;
        List<Ship> ships = getShips();

        for (Ship ship : ships) {
            if (ship.isAfloat()) {
                count++;
            }
        }

        return count;
    }

    public List<Coordinate> getAllShotsInField() {
        return this.allShotsInField;
    }

    public List<Coordinate> getEmptyCoordinates() {
        return this.emptyCoordinates;
    }

    public boolean containsEmptyCoordinates(Coordinate searchCoordinate) {
        List<Coordinate> emptyCoordinates = getEmptyCoordinates();

        for (Coordinate emptyCoordinate : emptyCoordinates) {
            if (emptyCoordinate.getHorizontal() == searchCoordinate.getHorizontal() &&
                    emptyCoordinate.getVertical() == searchCoordinate.getVertical())
            {
                return true;
            }
        }

        return false;
    }

    public List<Coordinate> getAllMistakes() {
        return this.allMistakes;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    /**
     * Метод добавляет только уникальные значения
     * @param coordinate
     */
    public void addAllShotsInField(Coordinate coordinate) {
        this.allShotsInField.add(coordinate);
    }

    public void addShip(Ship ship) {
        this.ships.add(ship);
    }

    /**
     * Метод удаляет текущую координату из списка "свободных/пустых" координат и добавляет её в список всех выстрелов
     * @param hitCoordinate координата попадания по кораблю.
     */
    public void addHit(Coordinate hitCoordinate) {
        /**
         * Удаление коориднаты из списка "пустых координат".
         */
        List<Coordinate> emptyCoordinates = getEmptyCoordinates();
        int hitCoordinateHorizontal = hitCoordinate.getHorizontal();
        int hitCoordinateVertical = hitCoordinate.getVertical();

        for (int i = 0; i < emptyCoordinates.size(); i++) {
            int emptyCoordinateHorizontal = emptyCoordinates.get(i).getHorizontal();
            int emptyCoordinateVertical = emptyCoordinates.get(i).getVertical();

            if (hitCoordinateHorizontal == emptyCoordinateHorizontal && hitCoordinateVertical == emptyCoordinateVertical) {
                emptyCoordinates.remove(i);
            }
        }

        // Добавление в список координат попаданий.
        allShotsInField.add(hitCoordinate);
    }

    /**
     * Метод удаляет текущую координату из списка "свободных/пустых" координат и добавляет её в список всех промахов
     * @param missCoordinate координата промаха.
     */
    public void addMiss(Coordinate missCoordinate) {
        /**
         * Удаление коориднаты из списка "пустых координат".
         */
        List<Coordinate> emptyCoordinates = getEmptyCoordinates();
        int missCoordinateHorizontal = missCoordinate.getHorizontal();
        int missCoordinateVertical = missCoordinate.getVertical();

        for (int i = 0; i < emptyCoordinates.size(); i++) {
            int emptyCoordinateHorizontal = emptyCoordinates.get(i).getHorizontal();
            int emptyCoordinateVertical = emptyCoordinates.get(i).getVertical();

            if (missCoordinateHorizontal == emptyCoordinateHorizontal && missCoordinateVertical == emptyCoordinateVertical) {
                emptyCoordinates.remove(i);
            }
        }

        // Добавление в список координат промахов.
        allMistakes.add(missCoordinate);
    }

    /**
     * Метод проверяет, есть ли данная координата у одного из кораблей на поле
     * @param verifiableCoordinate
     * @return
     */
    public boolean containsCoordinate(Coordinate verifiableCoordinate) {
        List<Coordinate> allShipsCoordinates = new ArrayList<>();

        for (Ship ship : this.ships) {
            allShipsCoordinates.addAll(ship.getAllDeckCoordinates());
        }

        for (Coordinate coordinate : allShipsCoordinates) {
            if (coordinate.getHorizontal() == verifiableCoordinate.getHorizontal() &&
                coordinate.getVertical() == verifiableCoordinate.getVertical()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Метод проверяет verifiableCoordinate на наличие её, в массиве уничтоженных палуб
     * @param verifiableCoordinate проверяемая координата
     * @return
     */
    public boolean containsDestroyedDecksCoordinate(Coordinate verifiableCoordinate) {
        List<Coordinate> allDestroyedDecksCoordinates = new ArrayList<>();

        for (Ship ship : this.ships) {
            List<Deck> decks = ship.getDecks();
            for (Deck deck : decks) {
                if (deck.getAfloat()) continue;
                allDestroyedDecksCoordinates.add(deck.getCoordinate());
            }
        }

        for (Coordinate coordinate : allDestroyedDecksCoordinates) {
            if (coordinate.getHorizontal() == verifiableCoordinate.getHorizontal() &&
                    coordinate.getVertical() == verifiableCoordinate.getVertical()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Метод отмечает координаты вокруг текущего корабля, как промахи
     * @param ship уничтоженный корабль
     * @return возвращает все координаты, куда больше нельзя стрелять игроку
     */
    public List<Coordinate> addMissAroundShip(Ship ship) {
        List<Coordinate> shipCoordinates = ship.getAllDeckCoordinates();
        List<Coordinate> playerShots = new ArrayList<>();

        // Перебор всех координат корабля
        for (Coordinate coordinate : shipCoordinates) {
            int horizontal = coordinate.getHorizontal();
            int vertical = coordinate.getVertical();

            List<Coordinate> verifiableCoordinates = new ArrayList<>();
            verifiableCoordinates.add(new Coordinate(horizontal, vertical));
            verifiableCoordinates.add(new Coordinate(horizontal, vertical - 1));
            verifiableCoordinates.add(new Coordinate(horizontal, vertical + 1));
            verifiableCoordinates.add(new Coordinate(horizontal - 1, vertical));
            verifiableCoordinates.add(new Coordinate(horizontal + 1, vertical));
            verifiableCoordinates.add(new Coordinate(horizontal - 1, vertical + 1));
            verifiableCoordinates.add(new Coordinate(horizontal - 1, vertical - 1));
            verifiableCoordinates.add(new Coordinate(horizontal + 1, vertical - 1));
            verifiableCoordinates.add(new Coordinate(horizontal + 1, vertical + 1));

            // Перебор всех направлений у координаты
            for (Coordinate verifiableCoordinate : verifiableCoordinates) {
                // Проверка на выход за пределы поля
                if (verifiableCoordinate.getHorizontal() > 9 || verifiableCoordinate.getHorizontal() < 0) continue;
                if (verifiableCoordinate.getVertical() > 9 || verifiableCoordinate.getVertical() < 0) continue;

                // Если в проверяемой коориднате есть корабль, то пропустить
                if (ship.containsCoordinate(verifiableCoordinate)) {
                    continue;
                }

                addMiss(verifiableCoordinate);
                playerShots.add(verifiableCoordinate);
            }
        }

        return playerShots;
    }

    /**
     * Метод отвечает за вывод поля на экран с отрисовкой:
     * 1. Кораблей;
     * 2. Промахов.
     */
    public void print(boolean isHidden) {
        int fieldSize = Settings.getFieldSize();

        String[][] field = new String[fieldSize][fieldSize];

        // Заполнение "зданего фона" у поля
        for (int row = 0; row < fieldSize; row++) {
            for (int col = 0; col < fieldSize; col++) {
                field[row][col] = "~";
            }
        }

        // Заполнение игрового поля кораблями, если оно не скрытое
        if ( !isHidden ) {
            for (Ship ship : this.ships) {
                // Добавление палуб, которые на палву
                List<Coordinate> afloatDecksCoordinates = ship.getAfloatDecksCoordinates();
                for (Coordinate coordinate : afloatDecksCoordinates) {
                    int horizontal = coordinate.getHorizontal();
                    int vertical = coordinate.getVertical();
                    field[vertical][horizontal] = "■";
                }
            }
        }
        // Добавление потопленных палуб
        for (Ship ship : this.ships) {

            List<Coordinate> destroyedDecksCoordinates = ship.getDestroyedDecksCoordinates();
            for (Coordinate coordinate : destroyedDecksCoordinates) {
                int horizontal = coordinate.getHorizontal();
                int vertical = coordinate.getVertical();
                field[vertical][horizontal] = "□";
            }
        }

        // Заполнение игрового поля "промахами"
        List<Coordinate> missCoordinates = getAllMistakes();

        for (Coordinate missCoordinate : missCoordinates) {
            int horizontal = missCoordinate.getHorizontal();
            int vertical = missCoordinate.getVertical();
            field[vertical][horizontal] = "·";
        }

        /**
         * Вывод игрового поля в консоль
         * Да, это реализовано ужасно
         */

        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
        System.out.print("   ");
        for (String letter : letters) {
            System.out.print(letter + "  ");
        }
        System.out.println();

        for (int row = 0; row < fieldSize; row++) {
            String numberInRow = Integer.toString(row + 1) + " ";
            if (row < 9) numberInRow += " ";

            System.out.print(numberInRow);

            for (int col = 0; col < fieldSize; col++) {
                System.out.print(field[row][col] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void print() {
        print(false);
    }
}
