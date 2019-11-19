package com.epam.azuredataimporter;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class Reporter implements ResultsObserver {
    private List<String> fails = new ArrayList<>();
    private List<String> warnings = new ArrayList<>();
    private LocalTime startTime;
    private int successfulOperations = 0;

    @Override
    public void failed(String message) {
        fails.add(message);
    }

    @Override
    public void warning(String message) {
        warnings.add(message);
    }

    public Reporter() {
        startTime = LocalTime.now();
    }

    @Override
    public void success() {
        successfulOperations++;
    }

    String getReport() {
        //Head
        StringBuilder report = new StringBuilder();
        report.append("Import report\n");
        report.append("Start time: ").append(startTime).append("\n");
        report.append("End time: ").append(LocalTime.now()).append("\n");
        report.append("Success operations: ").append(successfulOperations).append("\n");
        //Warnings
        if (warnings.size() != 0) {
            report.append("Warnings: \n");
            for (String warn : warnings)
                report.append(warn).append("\n");
        }
        //Errors
        report.append("Errors (" + fails.size() + "): \n");
        if (fails.size() == 0) report.append("Import completed without errors");
        else {
            for (String fail : fails)
                report.append(fail).append("\n");
        }
        return report.toString();
    }

    void writeReportToFile(String filename) {
        File report = new File(filename);
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(report)));
            writer.write(getReport());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
