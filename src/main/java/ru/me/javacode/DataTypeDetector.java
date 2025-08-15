package ru.me.javacode;

// класс-парсер
public class DataTypeDetector {
    public static DataType detectType(String line) {
        line = line.trim();
        if (line.isEmpty()) {
            return DataType.EMPTY;
        }

        String s = line.startsWith("+") || line.startsWith("-") ? line.substring(1) : line;

        if (s.matches("\\d+")) {
            return DataType.INTEGER;
        }
        if (s.matches("\\d+\\.\\d*") || s.matches("\\d*\\.\\d+")) {
            return DataType.FLOAT;
        }
        if (s.matches("\\d+(\\.\\d*)?[eE][+-]?\\d+")) {
            return DataType.FLOAT;
        }


        return DataType.STRING;
    }
}
