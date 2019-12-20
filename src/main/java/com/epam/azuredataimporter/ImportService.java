package com.epam.azuredataimporter;

import com.epam.azuredataimporter.daoimporting.BaseImportService;
import com.epam.azuredataimporter.parsing.ParseService;
import com.epam.azuredataimporter.reporting.ReportService;
import com.epam.azuredataimporter.reporting.ResultsObserver;
import com.epam.azuredataimporter.sources.FileSource;
import com.epam.azuredataimporter.spliting.LineReaderService;
import com.epam.azuredataimporter.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Queue;

@Component
public class ImportService {
    //////////////////////////////Services/////////////////////////////////
    @Autowired
    private FileSource sourceService;
    @Autowired
    private LineReaderService splitService;
    @Autowired
    private ParseService parseService;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private BaseImportService baseImportService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ResultsObserver observer;
    ///////////////////////////////////////////////////////////////////////

    private void endImporting() {
        String reportName = LocalTime.now().toString().split("[.]")[0].replace(':', '-') +
                "_" + LocalDate.now().toString().replace('-', '.') + "_importReport.txt";
        if (!reportService.sendReport(reportName)) reportService.writeReport(new File(reportName));
        reportService.resetLog();
    }

    public void startImport(ImportSequence importSequence) {
        while (importSequence.hasNext()) {
            ImportConfig config = importSequence.getNext();
            File sourceFile = sourceService.readFile(config.getCsv());
            if (sourceFile == null) {
                endImporting();
                return;
            }
            try {

                Queue<String> linesQueue = splitService.splitStream(new FileInputStream(sourceFile));
                Queue validationQueue = parseService.startAsyncParse(linesQueue);
                Queue baseImportQueue = validationService.startAsyncValidation(validationQueue);
                baseImportService.startAsyncImport(baseImportQueue);
                boolean splitDone = false;
                boolean parseDone = false;
                boolean validationDone = false;
                while (true) {
                    if (!splitDone && splitService.isDone()) {
                        parseService.endAsyncParse();
                        splitDone = true;
                        sourceFile.delete();
                        System.out.println("(Split) Done");
                    }
                    if (!parseDone && parseService.isDone()) {
                        validationService.endAsyncValidation();
                        parseDone = true;
                        System.out.println("(Parse) Done");
                    }
                    if (!validationDone && validationService.isDone()) {
                        baseImportService.endAsyncImport();
                        validationDone = true;
                        System.out.println("(Validation) Done");
                    }
                    if (baseImportService.isDone()) break;
                    Thread.sleep(1000);
                }
                System.out.println("(Import) Done");
            } catch (Exception e) {
                e.printStackTrace();
                observer.failed("(Critical) Internal error");
            }
            endImporting();
        }
    }
}
