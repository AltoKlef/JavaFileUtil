package ru.me.javacode;

import java.util.List;

public class NumberStats implements Stats {
    private final List<Double> numbers;
    private final Boolean full;
    public NumberStats(List<Double> numbers, Boolean full) {
        this.numbers = numbers;
        this.full = full;
    }

    @Override
    public String toString() {
        if(numbers.isEmpty()){
            return "No numbers";
        }
        if (!full) {
            return String.format("Count: %d", numbers.size());
        }
        double sum = numbers.stream().mapToDouble(Double::doubleValue).sum();
        double min = numbers.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double max = numbers.stream().mapToDouble(Double::doubleValue).max().orElse(0);
        double avg = sum / numbers.size();

        return String.format("Count: %d\nMin: %.2f\nMax: %.2f\nSum: %.2f\nAvg: %.2f\n",
                numbers.size(), min, max, sum, avg);

    }
}
