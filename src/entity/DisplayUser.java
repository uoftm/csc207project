package entity;

public class DisplayUser {
    private final String uid;
    private final String name;

    DisplayUser(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }
}
