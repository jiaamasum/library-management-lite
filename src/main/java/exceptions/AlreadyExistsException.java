package exceptions;

public class AlreadyExistsException extends LibraryException {
    public AlreadyExistsException(String what, String id) {
        super(what + " already exists: " + id);
    }
}
