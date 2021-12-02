package telegramBot.service;

import java.util.HashMap;
import java.util.Map;

public class CommandServiceImpl implements CommandService {

    private final Map<Long, String> commands = new HashMap<>();

    @Override
    public void setCommand(Long chatId, String command) {
        commands.put(chatId, command);
    }

    @Override
    public String getCommand(Long chatId) {
        return commands.getOrDefault(chatId, "");
    }
}
