package ru.me.javacode;

import java.util.List;

// класс для статистики строк
public class StringStats implements Stats {
    private int maxLength;
    private int minLength;
    private int size;
    private List<String> strings;
    private Boolean full;

    public StringStats(List<String> strings, Boolean full) {
        this.strings = strings;
        this.full = full;
    }

    @Override
    public void calculateStats(){
        this.size = strings.size();
        if(full){
            minLength = strings.stream().mapToInt(String::length).min().orElse(0);
            maxLength = strings.stream().mapToInt(String::length).max().orElse(0);
        }

    }
    @Override
    public String toString() {
        if (strings.isEmpty()) {
            return "No strings";
        }
        if (!full) {
            return String.format("Count: %d", size);
        }

        return String.format("Count: %d\nShortest: %d\nLongest: %d\n",
                size, minLength, maxLength);
    }
}