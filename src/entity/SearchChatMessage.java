package entity;

import java.util.List;

public class SearchChatMessage implements ChatMessage {
private final List<User> users;
private final String messageTime;
private final String roomID;
private final String message;

SearchChatMessage(List<User> room_users, String time, String roomID, String message){
    this.messageTime = time;
    this.users = room_users;
    this.roomID = roomID;
    this.message = message;

}

    @Override
    public String getTime() {
        return messageTime;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    public String getRoomID() {
        return roomID;
    }
}


