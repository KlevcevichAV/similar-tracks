package telegramBot;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import spotifySimilar.SimilarArtists;
import spotifySimilar.SimilarTracks;
import telegramBot.service.CommandService;
import telegramBot.service.CommandServiceImpl;

import java.util.List;
import java.util.Optional;

public class TgBot extends TelegramLongPollingBot {

    private final String TG_URL = "https://api.telegram.org/bot";
    private static SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setAccessToken("BQDiwjI7GzRJpEGnyj5R3EE6lpfOkCOlzLOtv2w2GLBkFGd0vJj_m10lSqcBwFU8bVt-2h35pl5SLB4cBBrWeDrAeBue7uXGIT8ZjDTv7hU9yi-Y-NkAbuaRDBJ-EyDkbSrcckjGaaG0QHUyYPRM8t9NQmx7-hI")
            .build();
    private final CommandService commandService = new CommandServiceImpl();

    @Override
    public String getBotUsername() {
        return "Similar tracks";
    }

    @Override
    public String getBotToken() {
        return "2144306800:AAHkIiXrpE9lqcBBiUG-T5VSSixbwnT4NyU";
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }
    }

    @SneakyThrows
    private void handleMessage(Message message) {

        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command =
                        message
                                .getText()
                                .substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                switch (command) {
                    case "/find_by_track_name": {
                        commandService.setCommand(message.getChatId(), "/find_by_track_name");
                        execute(SendMessage.builder()
                                .text("Введите название песни")
                                .chatId(message.getChatId().toString())
                                .build());
                        return;
                    }
                    case "/find_by_artist_name": {
                        commandService.setCommand(message.getChatId(), "/find_by_artist_name");
                        execute(SendMessage.builder()
                                .text("Введите имя артиста")
                                .chatId(message.getChatId().toString())
                                .build());
                        return;
                    }
                }
            }
        }
        if (message.hasText()) {
            Long chatId = message.getChatId();
            String lastCommand = commandService.getCommand(chatId);
            if (lastCommand != null) {
                switch (lastCommand) {
                    case "/find_by_track_name": {
                        String data = message.getText();
                        SimilarTracks similarTracks = new SimilarTracks(spotifyApi);
                        List<List<TrackSimplified>> listSimilarTracks = similarTracks.findSimilarTracks(data);
                        String responseMessage = similarTracks.getResponseMessage().toString();
                        execute(SendMessage.builder()
                                .text(responseMessage)
                                .chatId(chatId.toString())
                                .build());
                        return;
                    }
                    case "/find_by_artist_name": {
                        String data = message.getText();
                        SimilarArtists similarArtists = new SimilarArtists(spotifyApi);
                        List<Artist> listSimilarArtists = similarArtists.findSimilarArtists(data);
                        StringBuilder stringBuilder = new StringBuilder();
                        listSimilarArtists.forEach((e) -> stringBuilder.append(e.getName()).append("\n"));
                        execute(SendMessage.builder()
                                .text(stringBuilder.toString())
                                .chatId(chatId.toString())
                                .build());
                    }
                }
            } else {
                execute(SendMessage.builder()
                        .text("что-то пошло не так :(")
                        .chatId(chatId.toString())
                        .build());
            }
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        TgBot tgBot = new TgBot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(tgBot);
        System.out.println("finish registration");
    }
}
