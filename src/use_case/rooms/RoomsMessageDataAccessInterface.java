package use_case.rooms;

import entity.Message;
import java.util.List;

public interface RoomsMessageDataAccessInterface {
    void save(Message message);

    List<Message> getAllMessages();
}
