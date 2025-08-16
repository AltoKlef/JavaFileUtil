package ru.me.javacode;

import java.util.ArrayList;
import java.util.List;

//считает статистику для чисел
public class NumberStats implements Stats {

    private List<Double> numbers = new ArrayList<>();
    private List<String> numberStrings =  new ArrayList<>();
    private List<String> tooBigNumbers = new ArrayList<>();
    private boolean full;

    private boolean printBig = true; //можно отклюить печать всех больших чисел
    private int count;
    private double min;
    private double max;
    private double sum;
    private double avg;


    public NumberStats(List<String> numberStrings, boolean full) {
        this.full = full;
        this.numberStrings = numberStrings;
    }

    private void shortStat(){
        count = numberStrings.size();
    }

    //отделяет слишком большие числа
    private void parseNumbers() {
        for (String s : numberStrings) {
            try {
                double val = Double.parseDouble(s);
                if (Double.isInfinite(val) || Math.abs(val) > 1e18) {
                    tooBigNumbers.add(s);
                } else {
                    numbers.add(val);
                }
            } catch (NumberFormatException e) {
                tooBigNumbers.add(s);
            }
        }
    }

    //собственно статистика
    @Override
    public void calculateStats() {
        if (full) {
            parseNumbers();
            if (numbers.isEmpty()) return;

            min = Double.MAX_VALUE;
            max = -Double.MAX_VALUE;
            sum = 0;
            count = 0;

            List<Double> tooBig = new ArrayList<>();

            for (double val : numbers) {
                if (val < min) min = val;
                if (val > max) max = val;
                sum += val;
                count++;
            }

            avg = (count > 0) ? sum / count : 0;
        }
        else shortStat();
    }

    //удобный метод для вывода статистики (можно конечно сделать геттеры, но код небольшой, зачем)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (numbers.isEmpty()) {
            sb.append("No numbers\n");
        } else if (!full) {
            sb.append(String.format("Count: %d\n", count));
        } else {
            sb.append(String.format(
                    "Counted numbers: %d\nToo big numbers count: %d\nRecorded Numbers: %d\nMin: %.2f\nMax: %.2f\nSum: %.2f\nAvg: %.2f\n",
                    count, tooBigNumbers.size(), count +tooBigNumbers.size(), min, max, sum, avg
            ));
        }

        if (!tooBigNumbers.isEmpty() && printBig) {
            sb.append("Too big numbers (weren't counted in statistics):\n");
            tooBigNumbers.forEach(num -> sb.append("  ").append(num).append("\n"));
        }

        return sb.toString();
    }
    //настройка функций класса
    public boolean isPrintBig() {
        return printBig;
    }

    public void setPrintBig(boolean printBig) {
        this.printBig = printBig;
    }
    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }
}

