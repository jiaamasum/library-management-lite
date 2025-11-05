package thelibrarysystem;

import java.util.Objects;

public class Book implements Identifiable<String>, CsvSerializable {
    private final String id;       // e.g., ISBN or generated
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;

    public Book(String id, String title, String author, int totalCopies) {
        this.id = Objects.requireNonNull(id, "id");
        this.title = Objects.requireNonNull(title, "title");
        this.author = Objects.requireNonNull(author, "author");
        if (totalCopies < 1) throw new IllegalArgumentException("totalCopies must be >= 1");
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    @Override public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }

    public void setTitle(String title) { this.title = Objects.requireNonNull(title); }
    public void setAuthor(String author) { this.author = Objects.requireNonNull(author); }

    void decrementAvailable() { if (availableCopies > 0) availableCopies--; }
    void incrementAvailable() { if (availableCopies < totalCopies) availableCopies++; }

    @Override public String toString() {
        return String.format("Book{id='%s', title='%s', author='%s', %d/%d available}",
                id, title, author, availableCopies, totalCopies);
    }

    // --- CSV ---
    @Override public String toCsv() {
        return String.join(",",
                id,
                title.replace(",", " "),
                author.replace(",", " "),
                Integer.toString(totalCopies),
                Integer.toString(availableCopies));
    }

    public static Book fromCsv(String line) {
        String[] p = line.split(",", -1);
        Book b = new Book(p[0], p[1], p[2], Integer.parseInt(p[3]));
        b.availableCopies = Integer.parseInt(p[4]);
        return b;
    }
}
