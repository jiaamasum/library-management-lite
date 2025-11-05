package thelibrarysystem;

import java.time.LocalDate;
import java.util.UUID;

public class Loan implements Identifiable<String>, CsvSerializable {
    private final String id = UUID.randomUUID().toString();
    private final String bookId;
    private final String memberId;
    private final LocalDate checkoutDate;
    private LocalDate returnDate;

    public Loan(String bookId, String memberId) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.checkoutDate = LocalDate.now();
    }

    @Override public String getId() { return id; }
    public String getBookId() { return bookId; }
    public String getMemberId() { return memberId; }
    public LocalDate getCheckoutDate() { return checkoutDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public boolean isActive() { return returnDate == null; }
    public void markReturned() { this.returnDate = LocalDate.now(); }

    @Override public String toCsv() {
        return String.join(",", id, bookId, memberId, checkoutDate.toString(),
                returnDate == null ? "" : returnDate.toString());
    }

    public static Loan fromCsv(String line) {
        String[] p = line.split(",", -1);
        Loan l = new Loan(p[1], p[2]);
        // override generated fields for accurate load:
        try {
            var idField = Loan.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(l, p[0]);
        } catch (ReflectiveOperationException ignored) {}
        if (!p[4].isEmpty()) l.returnDate = LocalDate.parse(p[4]);
        return l;
    }

    @Override public String toString() {
        return "Loan{" + "bookId='" + bookId + '\'' +
                ", memberId='" + memberId + '\'' +
                ", checkoutDate=" + checkoutDate +
                (returnDate == null ? ", active" : ", returned=" + returnDate) + '}';
    }
}
