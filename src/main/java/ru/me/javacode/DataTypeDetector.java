package ru.me.javacode;

public class DataTypeDetector {
    public static DataType detectType(String line) {
        line = line.trim();
        if (line.isEmpty()) {
            //пустые строки решено пропускать
            return DataType.EMPTY;
        }

        try {
            Integer.parseInt(line);
            return DataType.INTEGER;
        } catch (NumberFormatException ignored) {}

        try {
            double d = Double.parseDouble(line);

            // Проверяем, целое ли число по значению (5.0 и 1e10 будут считаться целыми)
            if (d == Math.floor(d)) {
                return DataType.INTEGER;
            } else {
                return DataType.FLOAT;
            }
        } catch (NumberFormatException ignored) {}

        // Всё остальное — строка
        return DataType.STRING;
    }

}
