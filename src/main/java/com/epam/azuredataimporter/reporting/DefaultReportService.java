package com.epam.azuredataimporter.reporting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DefaultReportService implements ReportService {
    @Autowired
    private Reporter reporter;
    @Autowired
    private ReportSender sender;

    @Override
    public File getReport() {
        return reporter.getReportFile();
    }

    @Override
    public boolean sendReport(String reportName) {
        return sender.sendReport(reporter.getReportFile());
    }

    @Override
    public void resetLog() {
        reporter.resetLog();
    }

}
