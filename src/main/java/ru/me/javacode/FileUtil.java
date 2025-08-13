package ru.me.javacode;

import picocli.CommandLine;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "fileutil",
        description = "File Content filtering utility",
        mixinStandardHelpOptions = true
)
public class FileUtil implements Callable<Integer> {

    private final String intFileName = "integers.txt";
    private final String floatFileName = "floats.txt";
    private final String stringFileName = "strings.txt";
    private List<String> integers = new ArrayList<>();
    private List<String> floats = new ArrayList<>();
    private List<String> strings = new ArrayList<>();

    // Аргументы — входные файлы
    @CommandLine.Parameters(
            description = "Input files",
            paramLabel = "FILES"
    )
    private Path[] inputFiles;

    // Для результатов -o
    @CommandLine.Option(
            names = {"-o", "--outputDir"},
            description = " Directory for saving the results (default: ./)",
            defaultValue = "."
    )
    private Path outputDir;

    // Префикс файлов -p
    @CommandLine.Option(
            names = {"-p", "--prefix"},
            description = "Prefix for result files (default: blank)",
            defaultValue = ""
    )
    private String prefix;

    @CommandLine.Option(
            names = {"-a", "--append"},
            description = "Append mode for files"
    )
    private Boolean append = false;

    @CommandLine.Option(
            names = "-s",
            description = "Show short statistics"
    )
    Boolean shortStats = false;

    @CommandLine.Option(
            names = "-f",
            description = "Show full statistics"
    )
    Boolean fullStats = false;

    // метод записи в файл
    private void saveIfNotEmpty(Path filePath, List<String> data) {
        if (data.isEmpty()) {
            return;
        }
        try {
            Files.createDirectories(filePath.getParent());
            if (append) {
                Files.write(filePath, data, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } else {
                Files.write(filePath, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            }
            System.out.println("Saved file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error when writing a file " + filePath + ": " + e.getMessage());
        }
    }

    public void printStats() {
        boolean isFull = fullStats; // приоритет полного режима
        List<Double> integerNumbers = integers.stream()
                .map(Double::valueOf)
                .toList();
        List<Double> doubleNumbers = floats.stream().
                map(Double::valueOf).
                toList();
        Stats integerStats = new NumberStats(integerNumbers, isFull);
        Stats doubleStats = new NumberStats(doubleNumbers, isFull);
        Stats stringStats = new StringStats(strings, isFull);
        System.out.println("=== Integers stats ===");
        System.out.println(integerStats);
        System.out.println("=== Double stats ===");
        System.out.println(doubleStats);
        System.out.println("=== String stats ===");
        System.out.println(stringStats);
    }



    @Override
    public Integer call() {
        for (Path file : inputFiles) {
            if (!Files.exists(file)) {
                System.err.println("File not found: " + file);
                continue;
            }
            System.out.println("Reading file: " + file);

            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    DataType type = DataTypeDetector.detectType(line);
                    switch (type) {
                        case INTEGER -> integers.add(line);
                        case FLOAT   -> floats.add(line);
                        case STRING  -> strings.add(line);
                        case EMPTY   -> {}
                    }
                }
            } catch (IOException e) {
                System.err.println("Error when reading a file " + file + ": " + e.getMessage());
            }
        }

        Path integersFile = outputDir.resolve(prefix + intFileName);
        Path floatsFile   = outputDir.resolve(prefix + floatFileName);
        Path stringsFile  = outputDir.resolve(prefix + stringFileName);

        saveIfNotEmpty(integersFile, integers);
        saveIfNotEmpty(floatsFile, floats);
        saveIfNotEmpty(stringsFile, strings);

        if (fullStats || shortStats)
            printStats();

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new FileUtil()).execute(args);
        System.exit(exitCode);
    }
}

