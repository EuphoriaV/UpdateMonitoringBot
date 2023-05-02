import environment.IntegrationEnvironment;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataBaseTest extends IntegrationEnvironment {
    @Test
    public void testLinksTableCreate() {
        try {
            Connection connection = container.createConnection("");
            PreparedStatement statement = connection.prepareStatement("select * from links");
            var resultSet = statement.executeQuery();
            Set<String> linkFields = Set.of("link_id", "url", "checked_at");
            assertEquals(resultSet.getMetaData().getColumnCount(), linkFields.size());
            for (int i = 1; i <= linkFields.size(); i++) {
                assertTrue(linkFields.contains(resultSet.getMetaData().getColumnName(i)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testChatsTableCreate() {
        try {
            Connection connection = container.createConnection("");
            PreparedStatement statement = connection.prepareStatement("select * from chats");
            var resultSet = statement.executeQuery();
            Set<String> chatFields = Set.of("chat_id", "username");
            assertEquals(resultSet.getMetaData().getColumnCount(), chatFields.size());
            for (int i = 1; i <= chatFields.size(); i++) {
                assertTrue(chatFields.contains(resultSet.getMetaData().getColumnName(i)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testChatLinkTableCreate() {
        try {
            Connection connection = container.createConnection("");
            PreparedStatement statement = connection.prepareStatement("select * from chat_link");
            var resultSet = statement.executeQuery();
            Set<String> chatLinkFields = Set.of("link_id", "chat_id");
            assertEquals(resultSet.getMetaData().getColumnCount(), chatLinkFields.size());
            for (int i = 1; i <= chatLinkFields.size(); i++) {
                assertTrue(chatLinkFields.contains(resultSet.getMetaData().getColumnName(i)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
