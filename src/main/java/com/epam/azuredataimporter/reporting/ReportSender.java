package com.epam.azuredataimporter.reporting;

import java.io.File;

public interface ReportSender {
    boolean sendReport(File reportFile);

    boolean sendReport(String report, String reportName);
}
