package thelibrarysystem;

import exceptions.LibraryException;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Scanner in = new Scanner(System.in);
    private static final Library LIB = new Library();

    public static void main(String[] args) {
        try { LIB.load(); } catch (IOException ignored) {}
        seedIfEmpty();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = in.nextLine().trim();
            switch (choice) {
                case "1" -> addBook();
                case "2" -> addMember();
                case "3" -> listBooks();
                case "4" -> searchBooks();
                case "5" -> checkout();
                case "6" -> returnBook();
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        try { LIB.save(); } catch (IOException e) { System.out.println("Save failed: " + e.getMessage()); }
        System.out.println("Goodbye.");
    }

    private static void printMenu() {
        System.out.println("\n=== Library Menu ===");
        System.out.println("1) Add Book");
        System.out.println("2) Register Member");
        System.out.println("3) List Books (sorted)");
        System.out.println("4) Search Books");
        System.out.println("5) Checkout");
        System.out.println("6) Return Book");
        System.out.println("0) Exit");
        System.out.print("Choose: ");
    }

    private static void addBook() {
        System.out.print("Book ID: ");   String id = in.nextLine().trim();
        System.out.print("Title: ");     String title = in.nextLine().trim();
        System.out.print("Author: ");    String author = in.nextLine().trim();
        System.out.print("Total copies: "); int total = Integer.parseInt(in.nextLine().trim());
        try {
            LIB.addBook(new Book(id, title, author, total));
            System.out.println("Book added.");
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }

    private static void addMember() {
        System.out.print("Member ID: "); String id = in.nextLine().trim();
        System.out.print("Name: ");      String name = in.nextLine().trim();
        System.out.print("Email: ");     String email = in.nextLine().trim();
        try {
            LIB.registerMember(new Member(id, name, email));
            System.out.println("Member registered.");
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }

    private static void listBooks() {
        List<Book> books = LIB.listBooksSortedByTitle();
        books.forEach(System.out::println);
    }

    private static void searchBooks() {
        System.out.print("Search text (title/author contains): ");
        String q = in.nextLine().trim().toLowerCase();
        LIB.searchBooks(b -> b.getTitle().toLowerCase().contains(q) || b.getAuthor().toLowerCase().contains(q))
                .forEach(System.out::println);
    }

    private static void checkout() {
        System.out.print("Book ID: ");   String bookId = in.nextLine().trim();
        System.out.print("Member ID: "); String memberId = in.nextLine().trim();
        try {
            var loan = LIB.checkout(bookId, memberId);
            System.out.println("Checked out. Loan ID: " + loan.getId());
        } catch (LibraryException e) {
            System.out.println("Failed: " + e.getMessage());
        }
    }

    private static void returnBook() {
        System.out.print("Loan ID: "); String loanId = in.nextLine().trim();
        try {
            LIB.returnBook(loanId);
            System.out.println("Returned successfully.");
        } catch (LibraryException e) {
            System.out.println("Failed: " + e.getMessage());
        }
    }

    // tiny seed so the app isn't empty on first run
    private static void seedIfEmpty() {
        if (LIB.listBooksSortedByTitle().isEmpty()) {
            try {
                LIB.addBook(new Book("B-100", "Effective Java", "Joshua Bloch", 2));
                LIB.addBook(new Book("B-200", "Clean Code", "Robert C. Martin", 3));
                LIB.registerMember(new Member("M-1", "Masum", "masum@email.com"));
                LIB.registerMember(new Member("M-2", "Rahim", "rahim@email.com"));
            } catch (Exception ignored) {}
        }
    }


}

