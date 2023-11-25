package interface_adapter.chat;

import entity.Message;
import interface_adapter.ViewModel;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class ChatViewModel extends ViewModel {
  private ChatState state = new ChatState();
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  List<Message> messages;

  public ChatViewModel(List<Message> messages) {
    super("chat");
    this.messages = messages;
  }

  public void setState(ChatState state) {
    this.state = state;
  }

  public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  public ChatState getState() {
    return state;
  }

  public void firePropertyChanged() {
    support.firePropertyChange("messages", null, this.messages);
  }
}
