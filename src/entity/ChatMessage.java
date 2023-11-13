package entity;

import java.util.List;

public interface ChatMessage {
    public String getTime();
    public String getMessage();
    public List<User> getUsers();



}
