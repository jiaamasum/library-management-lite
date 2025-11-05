package thelibrarysystem;

import java.util.Objects;

public abstract class Person implements Identifiable<String> {
    protected final String id;
    protected String name;
    protected String email;

    protected Person(String id, String name, String email) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.email = Objects.requireNonNull(email, "email");
    }

    @Override public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public void setName(String name) { this.name = Objects.requireNonNull(name); }
    public void setEmail(String email) { this.email = Objects.requireNonNull(email); }

    @Override public String toString() {
        return String.format("%s{id='%s', name='%s'}", getClass().getSimpleName(), id, name);
    }
}