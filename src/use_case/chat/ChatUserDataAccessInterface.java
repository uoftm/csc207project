package use_case.chat;

import entities.auth.AbstractUser;

public interface ChatUserDataAccessInterface {
  AbstractUser get();
}
