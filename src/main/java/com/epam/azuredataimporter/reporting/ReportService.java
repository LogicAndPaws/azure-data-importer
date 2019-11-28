package com.epam.azuredataimporter.reporting;

import java.io.File;

public interface ReportService {
    String getReport();

    File writeReport(File filename);

    boolean sendReport(String reportName);

    void resetLog();
}
