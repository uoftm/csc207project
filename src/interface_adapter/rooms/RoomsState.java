package interface_adapter.rooms;

import entity.DisplayUser;
import entity.Message;

import java.util.List;

public class RoomsState {
    private String uid;
    private String name;
    private List<String> users;  // Contains usernames
    private List<Message> messages;

    public RoomsState(RoomsState copy) {
        uid = copy.uid;
        name = copy.name;
        users = copy.users;
        messages = copy.messages;
    }

    public RoomsState() {}

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUsers() {
        return users;
    }
    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<Message> getMessages() {
        return messages;
    }
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
