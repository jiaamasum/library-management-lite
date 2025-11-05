package thelibrarysystem;

import java.time.LocalDate;

public final class Member extends Person implements CsvSerializable {
    private final LocalDate joinedOn;

    public Member(String id, String name, String email) {
        super(id, name, email);
        this.joinedOn = LocalDate.now();
    }

    public LocalDate getJoinedOn() { return joinedOn; }

    // --- CSV ---
    @Override public String toCsv() {
        return String.join(",",
                id, name.replace(",", " "), email.replace(",", " "), joinedOn.toString());
    }

    public static Member fromCsv(String line) {
        String[] p = line.split(",", -1);
        return new Member(p[0], p[1], p[2]);
    }
}
