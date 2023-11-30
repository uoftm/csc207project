package data_access;

import entity.DisplayUser;
import entity.Message;
import use_case.rooms.RoomsDataAccessInterface;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FirebaseRoomsDataAccessObject implements RoomsDataAccessInterface {
    @Override
    public List<Message> getAllMessages() {
        DisplayUser dummy_display_user = new DisplayUser("foo", "bar");
        Instant timestamp = Instant.now();
        Message message = new Message(timestamp, "This is a test message.", dummy_display_user.getUid());

        List<Message> messages = new ArrayList<>();
        messages.add(message);
        return messages;
    }
}
