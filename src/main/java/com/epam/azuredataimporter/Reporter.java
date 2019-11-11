package com.epam.azuredataimporter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class Reporter implements ResultsObserver{
    private List<String> fails = new ArrayList<>();
    private int successfulOperations = 0;
    @Override
    public void failed(String message) {
        fails.add(message);
    }

    @Override
    public void success() {
        successfulOperations++;
    }

    public String getReport(){
        StringBuilder report = new StringBuilder();
        report.append("Import report");
        report.append("Date: ").append(LocalDate.now()).append("  ").append(LocalTime.now()).append("\n");
        report.append("Success operations: ").append(successfulOperations).append("\n");
        report.append("Errors: \n");
        if(fails.size()==0)report.append("Import completed without errors");
        else{
            for(String fail : fails)
                report.append(fail+"\n");
        }
        return report.toString();
    }
}
