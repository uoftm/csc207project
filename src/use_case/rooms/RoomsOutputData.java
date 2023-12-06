package use_case.rooms;

import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
import java.util.List;

public record RoomsOutputData(
    Room room, User user, List<Message> messages, String error, String success) {}
