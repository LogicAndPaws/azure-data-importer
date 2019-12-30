package com.epam.azuredataimporter.reporting;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class Reporter implements ResultsObserver {
    private List<String> fails = new ArrayList<>();
    private LocalTime startTime;
    private String currentFile = null;

    private int totalSuccessfulOperations = 0;
    private int currentFileSuccessfulOperations = 0;

    private File reportFile;
    private BufferedWriter writer;

    public Reporter() {
        try {
            reportFile = new File("report.txt");
            if (reportFile.exists()) {
                Files.delete(reportFile.toPath());
            }
            reportFile.createNewFile();
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reportFile)));
            startTime = LocalTime.now();
            writeHead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(String message) {
        fails.add(message);
    }

    @Override
    public void critical(String message) {

    }

    @Override
    public synchronized void success() {
        totalSuccessfulOperations++;
        currentFileSuccessfulOperations++;
    }

    @Override
    public void changeFile(String nextFile) {
        if (currentFile == null) {
            currentFile = nextFile;
            return;
        }
        writeBodyPart();
        currentFile = nextFile;
        currentFileSuccessfulOperations = 0;
        fails.clear();
    }

    private void writeHead() {
        try {
            writer.write("========Import Report========");
            writer.newLine();
            writer.write("Import date: " + LocalDate.now());
            writer.newLine();
            writer.write("Begin time: " + startTime.toString().split("[.]")[0]);
            writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeBodyPart() {
        try {
            writer.write("\t\t[" + currentFile + "]");
            writer.newLine();
            writer.write("Successful operations: " + currentFileSuccessfulOperations);
            writer.newLine();
            writer.write("Errors (" + fails.size() + "):");
            writer.newLine();
            if (fails.isEmpty()) {
                writer.write("Import completed without errors");
                writer.newLine();
            } else {
                for (String fail : fails) {
                    writer.write(fail);
                    writer.newLine();
                }
            }
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void writeTail() {
        try {
            writer.write("===========Results===========");
            writer.newLine();
            writer.write("End time: " + LocalTime.now().toString().split("[.]")[0]);
            writer.newLine();
            writer.write("Time on work: " + ChronoUnit.SECONDS.between(startTime, LocalTime.now()) + " sec");
            writer.newLine();
            writer.write("Total operations: " + totalSuccessfulOperations);
            writer.newLine();
            writer.write("Status: Import completed");
            currentFile = null;
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetLog() {
        try {
            fails.clear();
            startTime = LocalTime.now();
            writer.close();
            if (reportFile.exists()) {
                Files.delete(reportFile.toPath());
            }
            reportFile.createNewFile();
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reportFile)));
        } catch (IOException ignore) {
        }
    }


    public File getReportFile() {
        writeBodyPart();
        writeTail();
        return reportFile;
    }
}
