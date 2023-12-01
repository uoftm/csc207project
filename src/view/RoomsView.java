package view;

import entity.Message;
import interface_adapter.rooms.RoomsController;
import interface_adapter.rooms.RoomsState;
import interface_adapter.rooms.RoomsViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logged_in.LoggedInState;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.*;

public class RoomsView implements PropertyChangeListener {
    public JPanel contentPane;
    private JPanel paneInternals;
    private JScrollPane scrollPane;
    private JTextField message;
    private JButton send;
    private JPanel rawPane;
    private JLabel userUidLabel;
    private JLabel roomUidLabel;
    private final RoomsViewModel viewModel;

    public RoomsView(RoomsViewModel viewModel, RoomsController roomsController) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        rawPane.setLayout(new BoxLayout(rawPane, BoxLayout.Y_AXIS));

        paneInternals = new JPanel();
        paneInternals.setLayout(new BoxLayout(paneInternals, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(paneInternals);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(300, 300));

        rawPane.add(scrollPane);

        send.addActionListener(
                evt -> {
                    if (evt.getSource().equals(send)) {
                        RoomsState currentState = viewModel.getState();

                        String txt = currentState.getUserUid();
                        System.out.println(txt);
                    }
                });

        roomsController.loadMessages("", "");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RoomsState state = (RoomsState) evt.getNewValue();
        userUidLabel.setText(state.getUserUid());
    }
}
