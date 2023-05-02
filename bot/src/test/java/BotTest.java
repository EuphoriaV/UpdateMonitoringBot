import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.bot.UpdateMonitoringBot;
import ru.tinkoff.edu.java.scrapper.client.ScrapperClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BotTest {
    private static UpdateMonitoringBot bot;
    private static long testChatId;
    private static ScrapperClient scrapperClient;

    @BeforeAll
    public static void init() {
        scrapperClient = mock(ScrapperClient.class);
        bot = new UpdateMonitoringBot("5991824863:AAGLRPS01EfKLqfn-4KkGuwPlVj6kyln3jw", scrapperClient);
        testChatId = 795253839;
    }

    @Test
    public void testList() {
        Message list = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(list.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(testChatId);
        when(scrapperClient.getLinks(chat.id())).thenReturn(null);
        Message message = bot.list(list).message();
        assertEquals(message.text(), "Пока что отсутствуют отслеживаемые ссылки");
        assertEquals(message.entities()[0].type(), MessageEntity.Type.italic);
    }

    @Test
    public void testInvalidCommand() {
        Message invalid = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(invalid.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(testChatId);
        when(invalid.text()).thenReturn("abacabadabacaba");
        Message message = bot.handleInvalidMessage(invalid).message();
        assertEquals(message.text(), "Неизвестная команда: abacabadabacaba");
        assertEquals(message.entities()[0].type(), MessageEntity.Type.italic);
    }
}
