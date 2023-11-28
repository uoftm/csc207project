package data_access;

import entity.User;
import entity.UserFactory;
import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public class FileUserDataAccessObject
    implements SignupUserDataAccessInterface, LoginUserDataAccessInterface {

  private final File csvFile;

  private final Map<String, Integer> headers = new LinkedHashMap<>();

  private final Map<String, User> accounts = new HashMap<>();

  private final UserFactory userFactory;

  public FileUserDataAccessObject(String csvPath, UserFactory userFactory) throws IOException {
    this.userFactory = userFactory;

    csvFile = new File(csvPath);
    headers.put("email", 0);
    headers.put("username", 1);
    headers.put("password", 2);
    headers.put("creation_time", 3);

    if (csvFile.length() == 0) {
      save();
    } else {

      try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
        String header = reader.readLine();

        // For later: clean this up by creating a new Exception subclass and handling it in the UI.
        assert header.equals("email,username,password,creation_time");

        String row;
        while ((row = reader.readLine()) != null) {
          String[] col = row.split(",");
          String email = String.valueOf(col[headers.get("email")]);
          String username = String.valueOf(col[headers.get("username")]);
          String password = String.valueOf(col[headers.get("password")]);
          String creationTimeText = String.valueOf(col[headers.get("creation_time")]);
          LocalDateTime ldt = LocalDateTime.parse(creationTimeText);
          User user = userFactory.create(email, username, password, ldt);
          accounts.put(email, user);
        }
      }
    }
  }

  @Override
  public Optional<String> save(User user) {
    accounts.put(user.getEmail(), user);
    this.save();
    return Optional.empty();
  }

  @Override
  public Optional<User> get(String email, String password) { // For firebase auth (deprecate this file eventually)
    return Optional.ofNullable(accounts.get(email));
  }

  private void save() {
    BufferedWriter writer;
    try {
      writer = new BufferedWriter(new FileWriter(csvFile));
      writer.write(String.join(",", headers.keySet()));
      writer.newLine();

      for (User user : accounts.values()) {
        String line =
            String.format(
                "%s, %s,%s,%s",
                user.getEmail(), user.getName(), user.getPassword(), user.getCreationTime());
        writer.write(line);
        writer.newLine();
      }

      writer.close();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Return whether a user exists with username identifier.
   *
   * @param identifier the username to check.
   * @return whether a user exists with username identifier
   */
  @Override
  public boolean existsByName(String identifier) {
    return accounts.containsKey(identifier);
  }
}
