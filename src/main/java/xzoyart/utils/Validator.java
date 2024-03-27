package xzoyart.utils;

import org.apache.commons.lang3.StringUtils;
import xzoyart.game.Settings;
import xzoyart.game.coordinate.Coordinate;
import xzoyart.game.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Validator {
    /**
     * Метод валидирует координаты для постановки корабля.
     * 1. Аргументов в строке (через пробел) - 3;
     * 2. Перый аргумент должен быть определённой буквой;
     * 3. Второй аргумент должен быть числом;
     * 4. Второй аргумент должен входить в определённый числовой диапозон;
     * 5. Третий аргумент должен быть определённой буквой.
     * @param userInput ввод пользователя
     * @throws Exception
     */
    public static void placementCoordinatesValidate(String userInput) throws Exception {
        String[] splitedUserInput = userInput.split(" ");

        // Проверка на количество аргументов
        int argumentsCount = 3;
        if (splitedUserInput.length != argumentsCount) {
            throw new Exception("Неверное количество аргументов - " + splitedUserInput.length + ". Необходимое " +
                    "количество аргументов - " + argumentsCount);
        }

        // Проверка число ли во втором аргументе
        if ( ! StringUtils.isNumeric(splitedUserInput[1])) {
            throw new Exception("Во втором аргументе введено не число.");
        }

        // Связывание полученных координат со словами, для исключения "магических" чисел
        String letters = splitedUserInput[0];
        int number =  Integer.parseInt(splitedUserInput[1]);
        String vector = splitedUserInput[2];

        // Провека вектора
        if ( ! (vector.equalsIgnoreCase("v") || vector.equalsIgnoreCase("h"))) {
            throw new Exception("Неверно указан вектор. Ваше значение - \"" + vector + "\". Допустимые " +
                    "значения: \"v\", \"h\"");
        }

        // Проверка на границы поля
        if (number < 0 || number > 10) {
            throw new Exception("Корабль выходит за границы поля");
        }

        // Проверка на введённые "символы" пользователем
        String[] correctLetters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
        for (String letter: letters.split("")) {
            if (Character.isDigit(letter.charAt(0))) {
                throw new Exception(
                        "Первый аргумент должен состоять из букв" +
                                "\nРазрешены: \"a\", \"b\", \"c\", \"d\", \"e\", \"f\", \"g\", \"h\", \"i\", \"j\""
                );
            }
            if ( ! Arrays.asList(correctLetters).contains(letter)) {
                throw new Exception(
                        "Введенные символы не допустимы" +
                                "\nРазрешены: \"a\", \"b\", \"c\", \"d\", \"e\", \"f\", \"g\", \"h\", \"i\", \"j\""
                );
            }
        }
    }

    /**
     * Метод проверяет, чтобы вокруг текущей координаты (на 1 клетку во все стороны) не находилось кораблей.
     * @param coordinate проверяемая координата
     * @param field игровое поле
     * @throws Exception
     */
    public static void isEmptyAroundCell(Coordinate coordinate, Field field) throws Exception {
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

        for (Coordinate verifiableCoordinate : verifiableCoordinates) {
            if (field.containsCoordinate(verifiableCoordinate)) {
                throw new Exception("Поблизости уже есть другие корабли");
            }
        }
    }

    /**
     * Метод проверяет, выходит ли корабль за пределы игрового поля при его постановке.
     * @param coordinate начальные координаты корабля
     * @param deckCount количество палуб у корабля
     * @throws Exception
     */
    public static void shipLeavingField(Coordinate coordinate, int deckCount) throws Exception {
        String vector = coordinate.getVector();
        if (vector.equals("h") && (coordinate.getHorizontal() + deckCount) > Settings.getFieldSize()) {
            throw new Exception("Корабль выходит за пределы поля");
        }
        if (vector.equals("v") && (coordinate.getVertical() + deckCount) > Settings.getFieldSize()) {
            throw new Exception("Корабль выходит за пределы поля");
        }
    }

    public static void shotCoordinatesValidate(String userInput, Field field) throws Exception {
        String[] splitedUserInput = userInput.split(" ");

        // Проверка число ли во втором аргументе
        if ( ! StringUtils.isNumeric(splitedUserInput[1])) {
            throw new Exception("Во втором аргументе введено не число.");
        }

        // Проверка на количество аргументов
        int argumentsCount = 2;
        if (splitedUserInput.length != argumentsCount) {
            throw new Exception("Неверное количество аргументов - " + splitedUserInput.length + ". Необходимое " +
                    "количество аргументов - " + argumentsCount);
        }

        // Связывание полученных координат со словами, для исключения "магических" чисел
        String letters = splitedUserInput[0];
        int number =  Integer.parseInt(splitedUserInput[1]);

        // Проверка на границы поля
        if (number < 0 || number > 10) {
            throw new Exception("Корабль выходит за границы поля");
        }

        // Проверка на введённые "символы" пользователем
        String[] correctLetters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
        for (String letter: letters.split("")) {
            if (Character.isDigit(letter.charAt(0))) {
                throw new Exception(
                        "Первый аргумент должен состоять из букв" +
                                "\nРазрешены: \"a\", \"b\", \"c\", \"d\", \"e\", \"f\", \"g\", \"h\", \"i\", \"j\""
                );
            }
            if ( ! Arrays.asList(correctLetters).contains(letter)) {
                throw new Exception(
                        "Введенные символы не допустимы" +
                                "\nРазрешены: \"a\", \"b\", \"c\", \"d\", \"e\", \"f\", \"g\", \"h\", \"i\", \"j\""
                );
            }
        }
    }
}