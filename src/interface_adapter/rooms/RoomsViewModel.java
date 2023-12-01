package interface_adapter.rooms;

import entity.Message;
import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class RoomsViewModel extends ViewModel {
    private RoomsState state = new RoomsState();

    private String roomUid;

    private String userUid;

    public RoomsViewModel(String roomUid, String userUid) {
        super("chat");
        this.roomUid = roomUid;
        this.userUid = userUid;
    }

    public void setState(RoomsState state) {
        this.state = state;
    }

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public RoomsState getState() {
        return state;
    }
}
