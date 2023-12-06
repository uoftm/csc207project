package entities.auth;

public record DisplayUser(String email, String name) implements AbstractUser {}
