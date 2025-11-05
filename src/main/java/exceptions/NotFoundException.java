package exceptions;

public class NotFoundException extends LibraryException {
    public NotFoundException(String what, String id) {
        super(what + " not found: " + id);
    }
}