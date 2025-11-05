package exceptions;

public class NotAvailableException extends LibraryException {
    public NotAvailableException(String what) { super(what + " not available"); }
}
