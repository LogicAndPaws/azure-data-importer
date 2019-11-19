package com.epam.azuredataimporter;

import com.epam.azuredataimporter.sources.AzureConnector;
import com.epam.azuredataimporter.importing.PostgresDAO;
import com.epam.azuredataimporter.parsing.UserParser;
import com.epam.azuredataimporter.validation.UserValidator;

import java.io.*;
import java.time.LocalDate;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class MainLine {
    private Reporter reporter;
    private AzureConnector azureConnector;
    private PostgresDAO dao;
    private UserParser parser;
    private UserValidator validator;

    private ImportConfig config;

    private Queue<String> csvQueue  = new ArrayBlockingQueue<>(500);
    private Queue<Object> validationQueue = new ArrayBlockingQueue<>(500);
    private Queue<Object> baseImportQueue = new ArrayBlockingQueue<>(500);

    private boolean downloadDone = false;
    private int downloadCount = 0;
    private boolean parseDone = false;
    private int parseCount = 0;
    private boolean validationDone = false;
    private int validationCount = 0;
    private boolean importDone = false;
    private int importCount = 0;

    private Thread[] threads;

    private Runnable startDownload = new Runnable() {
        @Override
        public void run() {
            File file = azureConnector.readFile(config.targetFile);
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(reader==null){
                stopImport();
                return;
            }
            while (true) {
                try {
                    if (!reader.ready()) break;
                    if(csvQueue.size()<500)
                    csvQueue.offer(reader.readLine());
                    else continue;
                    downloadCount++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                reader.close();
                file.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
            downloadDone = true;
            System.out.println("Download done " + downloadCount );
        }
    };
    /*
    private Runnable startParse = new Runnable() {
        @Override
        public void run() {
            while (true){
                try{
                    if(csvQueue.isEmpty() && downloadDone)break;
                    if (csvQueue.isEmpty()){
                        Thread.sleep(10);
                        continue;
                    }
                    User nextUser = parser.parseUser(csvQueue.remove());
//                    System.out.println("user("+nextUser.getId()+") parsed");
                    if(nextUser!=null && nextUser.getName()==null)stopImport();
                    if(nextUser==null)continue;
                    while(validationQueue.size()>=500)Thread.sleep(10);
                    validationQueue.offer(nextUser);
                    parseCount++;
                } catch (InterruptedException e) {
                    return;
                }
            }
            parseDone=true;
//            System.out.println(reporter.getReport());
            System.out.println("Parse done " + parseCount);
        }
    };
    private Runnable startValidation = new Runnable() {
        @Override
        public void run() {
            while(true){
                try{
                    if(validationQueue.isEmpty() && parseDone)break;
                    if (validationQueue.isEmpty()){
                        Thread.sleep(10);
                        continue;
                    }
                    User nextUser = (User)validationQueue.remove();
                    if(validator.validUser(nextUser)) {
                        while (baseImportQueue.size()>=500)Thread.sleep(10);
                        baseImportQueue.offer(nextUser);
                        validationCount++;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
            validationDone = true;
            System.out.println("Validation done " + validationCount);
//            System.out.println(reporter.getReport());
        }
    };
    private Runnable startBaseImport = new Runnable() {
        @Override
        public void run() {
            while(true){
                try {
                    if(baseImportQueue.isEmpty() && validationDone)break;
                    if (baseImportQueue.isEmpty()){
                        Thread.sleep(10);
                        continue;
                    }
//                    System.out.println("Inserting user(" + ((User)baseImportQueue.peek()).getId() + ")...");
                    dao.insertUser((User)baseImportQueue.remove());
                    importCount++;
                } catch (InterruptedException e) {
                    return;
                }
            }
            importDone = true;
            System.out.println("Import done " + importCount);
        }
    };
    */
    private void stopImport(){
        for(Thread thread : threads)
            thread.interrupt();
        System.out.println("Critical stop");
    }

    public MainLine(Reporter reporter, AzureConnector connector, PostgresDAO dao, UserParser parser, UserValidator validator){
        this.reporter = reporter;
        azureConnector = connector;
        this.dao = dao;
        this.parser = parser;
        this.validator = validator;
    }
    private void sendReport(){
        if(!importDone)reporter.failed("Import is'nt complete case of critical errors");
        if(!azureConnector.sendStringToFile(reporter.getReport(),"importReport-"+config.targetFile+".txt"))
            reporter.writeReportToFile("importReport"+LocalDate.now()+".txt");
    }
    public void startImport(){
        String trigger = azureConnector.getTrigger();
        if(trigger==null){
            sendReport();
            return;}
        config = parser.parseTrigger(trigger);
        if(config!=null) {
            System.out.println("targetFile = "+config.targetFile);

            for (Thread thread : threads)
                thread.start();
            try {
                threads[3].join();
                azureConnector.deleteTrigger();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
       sendReport();
    }
}
