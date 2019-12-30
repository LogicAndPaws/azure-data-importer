package com.epam.azuredataimporter.reporting;

import java.io.File;

public interface ReportService {
    File getReport();

    boolean sendReport(String reportName);

    void resetLog();
}
