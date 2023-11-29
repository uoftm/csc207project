import static java.lang.Thread.sleep;

import app.ChatUseCaseFactory;
import data_access.FirebaseMessageDataAccessObject;
import entity.CommonUserFactory;
import entity.User;
import entity.UserFactory;
import interface_adapter.chat.ChatViewModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.*;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Test;
import use_case.chat.ChatMessageDataAccessInterface;
import use_case.chat.ChatUserDataAccessInterface;
import view.ChatView;

public class ChatTest {
  @Test
  public void displaysMessages() {
    OkHttpClient client = new OkHttpClient();
    User user = new CommonUserFactory().create("test-user-id", "example@example.com","test-user-name", "password", LocalDateTime.now());
    ChatMessageDataAccessInterface messageDataAccessObject =
        new FirebaseMessageDataAccessObject(client);
    var chatViewModel = new ChatViewModel(new ArrayList<>());
    ChatView view = ChatUseCaseFactory.create(messageDataAccessObject, chatViewModel, () -> user);

    JFrame jf = new JFrame();
    jf.setContentPane(view.contentPane);
    jf.pack();
    jf.setVisible(true);

    try {
      sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    Assert.assertTrue(!chatViewModel.messages.isEmpty());
  }
}
