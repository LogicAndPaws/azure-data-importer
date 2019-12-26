package com.epam.azuredataimporter;

import com.epam.azuredataimporter.daoimporting.BaseImportService;
import com.epam.azuredataimporter.entity.Entity;
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
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Queue;

@Component
public class ImportService<T extends Entity> {
    //////////////////////////////Services/////////////////////////////////
    @Autowired
    private FileSource sourceService;
    @Autowired
    private LineReaderService splitService;
    @Autowired
    private ParseService<T> parseService;
    @Autowired
    private ValidationService<T> validationService;
    @Autowired
    private BaseImportService<T> baseImportService;
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
            System.out.println("Importing " + config.getCsv() + "...");
            File sourceFile = sourceService.readFile(config.getCsv());
            if (sourceFile == null) {
                endImporting();
                return;
            }
            try {
                Queue<String> linesQueue = splitService.splitStream(new FileInputStream(sourceFile));
                Queue<T> validationQueue = parseService.startAsyncParse(linesQueue, config.getClazz());
                Queue<T> baseImportQueue = validationService.startAsyncValidation(validationQueue);
                baseImportService.startAsyncImport(baseImportQueue);
                boolean splitDone = false;
                boolean parseDone = false;
                boolean validationDone = false;
                while (true) {
                    if (!splitDone && splitService.isDone()) {
                        parseService.endAsyncParse();
                        splitDone = true;
                        Files.delete(sourceFile.toPath());
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
        }
        endImporting();
        System.out.println("Import complete!");
    }
}
