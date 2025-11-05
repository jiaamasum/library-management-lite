package thelibrarysystem;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public final class FileStorage {
    private FileStorage() {}

    public static final String BOOKS_FILE   = "data/books.csv";
    public static final String MEMBERS_FILE = "data/members.csv";
    public static final String LOANS_FILE   = "data/loans.csv";

    public static void saveAll(String file, Collection<? extends CsvSerializable> items) throws IOException {
        Path p = Paths.get(file);
        Files.createDirectories(p.getParent());
        List<String> lines = items.stream().map(CsvSerializable::toCsv).toList();
        Files.write(p, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static <T> List<T> loadAll(String file, Function<String, T> parser) throws IOException {
        Path p = Paths.get(file);
        if (!Files.exists(p)) return new ArrayList<>();
        List<String> lines = Files.readAllLines(p);
        List<T> out = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            out.add(parser.apply(line));
        }
        return out;
    }
}
