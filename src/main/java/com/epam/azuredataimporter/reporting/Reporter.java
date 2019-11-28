package com.epam.azuredataimporter.reporting;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class Reporter implements ResultsObserver {
    private List<String> fails = new ArrayList<>();
    private LocalTime startTime;
    private int successfulOperations = 0;

    public Reporter() {
        startTime = LocalTime.now();
    }

    @Override
    public void failed(String message) {
        fails.add(message);
    }

    @Override
    public synchronized void success() {
        successfulOperations++;
    }

    public void resetLog() {
        fails.clear();
        startTime = LocalTime.now();
    }

    public String getReport() {
        //Head
        StringBuilder report = new StringBuilder();
        report.append("Import report\n");
        report.append("Import date: ").append(LocalDate.now()).append("\n");
        report.append("Start time: ").append(startTime.toString().split("[.]")[0]).append("\n");
        report.append("End time: ").append(LocalTime.now().toString().split("[.]")[0]).append("\n");
        ////DEBUG////
        report.append("Time on work: ").append(ChronoUnit.SECONDS.between(startTime, LocalTime.now())).append(" sec\n");
        ////DEBUG////
        report.append("Success operations: ").append(successfulOperations).append("\n");
        //Errors
        report.append("Errors (").append(fails.size()).append("): \n");
        if (fails.isEmpty()) report.append("Import completed without errors");
        else {
            for (String fail : fails)
                report.append(fail).append("\n");
        }
        return report.toString();
    }


}
