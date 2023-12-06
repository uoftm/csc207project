package interface_adapter.room_settings;

import entities.auth.User;
import entities.rooms.Room;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class RoomSettingsViewModel {
  private Room activeRoom;
  private User user;
  private String error;

  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  public void setActiveRoom(Room activeRoom) {
    this.activeRoom = activeRoom;
    support.firePropertyChange("activeRoom", null, activeRoom);
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void setError(String error) {
    this.error = error;
    support.firePropertyChange("error", null, error);
  }

  public String getError() {
    return error;
  }

  public Room getActiveRoom() {
    return activeRoom;
  }

  public User getUser() {
    return user;
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }
}