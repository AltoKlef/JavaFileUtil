package ru.me.javacode;

import java.util.List;

// класс для статистики строк
public class StringStats implements Stats {
    private List<String> strings;
    private Boolean full;

    public StringStats(List<String> strings, Boolean full) {
        this.strings = strings;
        this.full = full;
    }

    @Override
    public String toString() {
        if (strings.isEmpty()) {
            return "No strings";
        }
        if (!full) {
            return String.format("Count: %d", strings.size());
        }
        int minLength = strings.stream().mapToInt(String::length).min().orElse(0);
        int maxLength = strings.stream().mapToInt(String::length).max().orElse(0);

        return String.format("Count: %d\n Shortest: %d\nLongest: %d\n",
                strings.size(), minLength, maxLength);
    }
}