package xzoyart.game;

import xzoyart.game.bots.Bot;
import xzoyart.game.coordinate.Coordinate;
import xzoyart.game.factories.BotFactory;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public Player player = new Player();
    public Bot bot;

    public Game() {
        this.bot = BotFactory.createBot(Settings.getBotDifficulty()); // Объявление экземпляра бота
    }

    public void autoPlacement() {
        // первый корабль
        Ship shipForPlayer1 = new Ship(4);
        List<Deck> decksForPlayer1 = shipForPlayer1.getDecks();
        List<Coordinate> coordinatesForPlayer1 = new ArrayList<>();
        coordinatesForPlayer1.add(new Coordinate(0, 0, "h"));
        coordinatesForPlayer1.add(new Coordinate(1, 0, "h"));
        coordinatesForPlayer1.add(new Coordinate(2, 0, "h"));
        coordinatesForPlayer1.add(new Coordinate(3, 0, "h"));
        decksForPlayer1.get(0).setCoordinate(coordinatesForPlayer1.get(0));
        decksForPlayer1.get(1).setCoordinate(coordinatesForPlayer1.get(1));
        decksForPlayer1.get(2).setCoordinate(coordinatesForPlayer1.get(2));
        decksForPlayer1.get(3).setCoordinate(coordinatesForPlayer1.get(3));
        player.getField().addShip(shipForPlayer1);
    }

    public void start() {
        instruction();
        player.getField().print();
        player.shipPlacement();
        bot.shipPlacement();
        battle();
    }

    /**
     * Метод отвечает за сражение между игроком и ботом
     */
    public void battle() {
        String winner = "";

        while (true) {
            // Ход игрока
            String shotResultPlayer = player.shot(bot.getField());
            System.out.println();

            // Проверка на проигрыш бота
            if (this.bot.getField().getShipsCount() == 0) {
                winner = "Player";
                break;
            }

            // Ход бота
            String shotResultBot = bot.shot(player.getField());
            System.out.println();

            // Вывод результатов выстрела
            this.player.getField().print();
            System.out.println("Бот стреляет и ... " + shotResultBot);
            System.out.println("Количество кораблей у игрока: " + player.getField().getShipsCount());
            System.out.println();
            this.bot.getField().print(true);
            System.out.println("Игрок стреляет и ... " + shotResultPlayer);
            System.out.println("Количество кораблей у бота: " + bot.getField().getShipsCount());
            System.out.println("\n\n");

            // Проверка на проигрыш игрока
            if (this.player.getField().getShipsCount() == 0) {
                winner = "Bot";
                break;
            }
        }

        System.out.println("Конец игры.");
        System.out.println("Победил - " + winner + "!");
    }

    public static void instruction() {
        System.out.println();
        System.out.println("Координаты постановки должны выглядеть следующим образом:");
        System.out.println("a 1 v (где \"v\" или \"h\" это вектор)");
        System.out.println();
        System.out.println("Координаты для выстрела должны выглядеть следующим образом:");
        System.out.println("b 1");
        System.out.println();
        System.out.println("Сложность бота можно выбрать в Settings.java");
        System.out.println();
    }
}