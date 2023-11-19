package interface_adapter.chat;

import entity.Message;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class ChatViewModel {
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  List<Message> messages;

  public ChatViewModel(List<Message> messages) {
    this.messages = messages;
  }

  public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  public void firePropertyChanged() {
    support.firePropertyChange("messages", null, this.messages);
  }
}
