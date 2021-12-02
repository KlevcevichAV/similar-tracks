package telegramBot.service;

public interface CommandService {

    void setCommand(Long chatId, String command);

    String getCommand(Long chatId);

}
