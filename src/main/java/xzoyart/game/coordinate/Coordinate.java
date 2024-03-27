package xzoyart.game.coordinate;

import java.io.Serializable;

public class Coordinate implements Serializable {
    private int horizontal;
    private int vertical;
    private String vector = null;

    public Coordinate(int horizontal, int vertical, String vector) {
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.vector = vector;
    }

    public Coordinate(int horizontal, int vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public Coordinate() {
        this.horizontal = 0;
        this.vertical = 0;
    }

    public int getHorizontal() {
        return this.horizontal;
    }

    public int getVertical() {
        return this.vertical;
    }

    public String getVector() {
        return this.vector;
    }

    public void setHorizontal(int horizontal) {
        this.horizontal = horizontal - 1;
    }

    public void setVertical(int vertical) {
        this.vertical = vertical - 1;
    }

    public void setVector(String vector) {
        this.vector = vector;
    }

    public void verticalIncrement() {
        this.vertical++;
    }

    public void horizontalIncrement() {
        this.horizontal++;
    }

    /**
     * Метод присвает текущему объекту значения: horizontal, vertical, vector.
     * Преобразует букву letter в цифру!
     * @param number принимает число в строковом представлении
     * @param letter принимает букву
     * @param vector принмает букву вектора
     * @throws Exception
     */
    public void create(String letter, String number, String vector) {
        this.horizontal = letterToNumber(letter);
        this.vertical = Integer.parseInt(number) - 1; // Вычитается 1, для корректной адресации к массиву
        this.vector = vector;
    }

    /**
     * Метод присвает текущему объекту значения: horizontal, vertical.
     * Vector - null
     * @param number принимает число в строковом представлении
     * @param letter принимает букву
     * @throws Exception
     */
    public void create(String number, String letter) {
        create(number, letter, null);
    }

    /**
     * Метод переводит букву в цифру, начиная с "a" => 0 и далее по алфавиту, заканчивая на "j".
     * @param letter буква, которую необходимо преобразовать в число
     * @return число, эквивалентное номеру буквы в алфавите
     */
    public static int letterToNumber(String letter) {
        String correctLetters = "abcdefghij";
        int number = correctLetters.indexOf(letter);

        return number;
    }
}
