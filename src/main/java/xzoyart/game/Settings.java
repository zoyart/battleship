package xzoyart.game;

import java.util.ArrayList;
import java.util.List;

public class Settings {
    /**
     * Размеры игрового поля
     */
    private static int fieldSize = 10;

    /**
     * При изменении названий самих градаций, необходимо поменять их названия BotFactory.
     * easy - bot каждый ход наносит удары по случайным полям и не старается добить корабль.
     * medium - bot старается добить корабль по которому попал, имитирую поведение человека.
     * hard - bot при попадании по корблю знает где его оставшиеся части и добивает их.
     */
    private static String botDifficulty = "medium";

    public static int getFieldSize() {
        return fieldSize;
    }

    public static String getBotDifficulty() {
        return botDifficulty;
    }
}
