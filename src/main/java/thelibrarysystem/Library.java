package thelibrarysystem;

import exceptions.*;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Library {
    private final Map<String, Book> books = new HashMap<>();
    private final Map<String, Member> members = new HashMap<>();
    private final Map<String, Loan> loans = new HashMap<>(); // by loanId
    private final Map<String, List<Loan>> loansByBook = new HashMap<>();
    private final Map<String, List<Loan>> loansByMember = new HashMap<>();

    // ---------- CRUD ----------
    public void addBook(Book b) throws AlreadyExistsException {
        if (books.containsKey(b.getId())) throw new AlreadyExistsException("Book", b.getId());
        books.put(b.getId(), b);
    }

    public void registerMember(Member m) throws AlreadyExistsException {
        if (members.containsKey(m.getId())) throw new AlreadyExistsException("Member", m.getId());
        members.put(m.getId(), m);
    }

    public List<Book> listBooksSortedByTitle() {
        return books.values().stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooks(Predicate<Book> filter) {
        return books.values().stream().filter(filter).collect(Collectors.toList());
    }

    // ---------- Borrow / Return ----------
    public Loan checkout(String bookId, String memberId)
            throws LibraryException {
        Book b = books.get(bookId);
        if (b == null) throw new NotFoundException("Book", bookId);
        if (b.getAvailableCopies() <= 0) throw new NotAvailableException("Book copies");

        Member m = members.get(memberId);
        if (m == null) throw new NotFoundException("Member", memberId);

        Loan l = new Loan(bookId, memberId);
        loans.put(l.getId(), l);
        loansByBook.computeIfAbsent(bookId, k -> new ArrayList<>()).add(l);
        loansByMember.computeIfAbsent(memberId, k -> new ArrayList<>()).add(l);
        b.decrementAvailable();
        return l;
    }

    public void returnBook(String loanId) throws LibraryException {
        Loan l = loans.get(loanId);
        if (l == null) throw new NotFoundException("Loan", loanId);
        if (!l.isActive()) throw new NotAvailableException("Loan already returned");
        l.markReturned();
        Book b = books.get(l.getBookId());
        b.incrementAvailable();
    }

    // ---------- Persistence ----------
    public void save() throws IOException {
        FileStorage.saveAll(FileStorage.BOOKS_FILE, books.values());
        FileStorage.saveAll(FileStorage.MEMBERS_FILE, members.values());
        FileStorage.saveAll(FileStorage.LOANS_FILE, loans.values());
    }

    public void load() throws IOException {
        books.clear(); members.clear(); loans.clear();
        loansByBook.clear(); loansByMember.clear();

        FileStorage.loadAll(FileStorage.BOOKS_FILE, Book::fromCsv)
                .forEach(b -> books.put(b.getId(), b));

        FileStorage.loadAll(FileStorage.MEMBERS_FILE, Member::fromCsv)
                .forEach(m -> members.put(m.getId(), m));

        FileStorage.loadAll(FileStorage.LOANS_FILE, Loan::fromCsv).forEach(l -> {
            loans.put(l.getId(), l);
            loansByBook.computeIfAbsent(l.getBookId(), k -> new ArrayList<>()).add(l);
            loansByMember.computeIfAbsent(l.getMemberId(), k -> new ArrayList<>()).add(l);
        });
    }

    // ---------- Helpers / Reports ----------
    public Optional<Book> findBookById(String id) { return Optional.ofNullable(books.get(id)); }
    public Optional<Member> findMemberById(String id) { return Optional.ofNullable(members.get(id)); }
    public Optional<Loan> findLoanById(String id) { return Optional.ofNullable(loans.get(id)); }

    public List<Loan> activeLoansForMember(String memberId) {
        return loansByMember.getOrDefault(memberId, List.of()).stream()
                .filter(Loan::isActive).collect(Collectors.toList());
    }

    public List<Book> topAvailable(int limit) {
        return books.values().stream()
                .sorted(Comparator.comparingInt(Book::getAvailableCopies).reversed())
                .limit(limit).toList();
    }
}
