package interface_adapter.chat;

import interface_adapter.logged_in.LoggedInState;

public class ChatState {
    public String message = "";

    public ChatState(ChatState copy) {
        message = copy.message;
    }

    public ChatState() {}
}
