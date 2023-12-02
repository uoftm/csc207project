package use_case.signup;

import java.time.Instant;

public class SignupOutputData {

  private final String email;
  private final Instant creationTime;

  public SignupOutputData(String email, Instant creationTime) {
    this.email = email;
    this.creationTime = creationTime;
  }

  public String getEmail() {
    return email;
  }

  public Instant getCreationTime() {
    return creationTime;
  }
}
