package com.epam.azuredataimporter.reporting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class DefaultReportService implements ReportService {
    @Autowired
    private Reporter reporter;
    @Autowired
    private ReportSender sender;

    @Override
    public String getReport() {
        return reporter.getReport();
    }

    @Override
    public File writeReport(File file) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
            writer.write(getReport());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    @Override
    public boolean sendReport(String reportName) {
        return sender.sendReport(reporter.getReport(), reportName);
    }

    @Override
    public void resetLog() {
        reporter.resetLog();
    }

}
