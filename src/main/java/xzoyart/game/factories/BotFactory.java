package xzoyart.game.factories;

import xzoyart.game.bots.Bot;
import xzoyart.game.bots.EasyBot;
import xzoyart.game.bots.MediumBot;
import xzoyart.game.bots.HardBot;

public class BotFactory {
    /**
     * Метод создаёт экземпляр бота с необходимой сложностью
     * @param botDifficulty сложность бота
     * @return возвращает экземпляр класса Bot
     */
    public static Bot createBot(String botDifficulty) {
        Bot bot = null;

        switch (botDifficulty) {
            case "easy":
                bot = new EasyBot();
                break;
            case "medium":
                bot = new MediumBot();
                break;
            case "hard":
                bot = new HardBot();
                break;
        }

        return bot;
    }
}
