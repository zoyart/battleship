package xzoyart.utils;

import java.util.Scanner;

public class Input {
    /**
     * Метод возвращает ввод пользователя.
     * @param text текст перед вводом пользователя.
     * @return ввод пользователя.
     */
    public static String userInput(String text) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(text + ": ");

        return scanner.nextLine().trim();
    }

    /**
     * Метод возвращает ввод пользователя.
     * По умолчанию выводит на экран "Input: ".
     * @return ввод пользователя.
     */
    public static String userInput() {
        return userInput("Input");
    }
}
