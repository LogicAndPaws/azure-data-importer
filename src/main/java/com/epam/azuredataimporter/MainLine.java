package com.epam.azuredataimporter;

import com.epam.azuredataimporter.azure.AzureConnector;
import com.epam.azuredataimporter.dao.PostgresDAO;
import com.epam.azuredataimporter.data.DataParser;
import com.epam.azuredataimporter.data.DataValidator;
import com.epam.azuredataimporter.entity.User;

import java.io.*;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class MainLine {
    private Reporter reporter;
    private AzureConnector azureConnector;
    private PostgresDAO dao;
    private DataParser parser;
    private DataValidator validator;

    private ImportConfig config;

    private Queue<String> csvQueue  = new ArrayBlockingQueue<>(100);
    private Queue<Object>  validationQueue = new ArrayBlockingQueue<>(100);
    private Queue<Object> baseImportQueue = new ArrayBlockingQueue<>(100);

    private boolean downloadDone = false;
    private boolean parseDone = false;
    private boolean validationDone = false;
    private boolean importDone = false;

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
                    csvQueue.offer(reader.readLine());
//                    System.out.println(csvQueue.peek());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            downloadDone = true;
            System.out.println("Download done");
        }
    };
    private Runnable startParse = new Runnable() {
        @Override
        public void run() {
            while (true){
                try{
                    if(csvQueue.isEmpty() && downloadDone)break;
                    if (csvQueue.isEmpty()){
                        Thread.sleep(100);
                        continue;
                    }
                    User nextUser = parser.parseUser(csvQueue.remove());
//                    System.out.println("user("+nextUser.getId()+") parsed");
                    if(nextUser!=null && nextUser.getName()==null)stopImport();
                    if(nextUser==null)continue;
                    validationQueue.offer(nextUser);
                } catch (InterruptedException e) {
                    return;
                }
            }
            parseDone=true;
//            System.out.println(reporter.getReport());
            System.out.println("Parse done");
        }
    };
    private Runnable startValidation = new Runnable() {
        @Override
        public void run() {
            while(true){
                try{
                    if(validationQueue.isEmpty() && parseDone)break;
                    if (validationQueue.isEmpty()){
                        Thread.sleep(100);
                        continue;
                    }
                    User nextUser = (User)validationQueue.remove();
                    if(validator.validUser(nextUser))baseImportQueue.offer(nextUser);
                } catch (InterruptedException e) {
                    return;
                }
            }
            validationDone = true;
            System.out.println("Validation done");
//            System.out.println(reporter.getReport());
        }
    };
    private Runnable startImport = new Runnable() {
        @Override
        public void run() {
            while(true){
                try {
                    if(baseImportQueue.isEmpty() && validationDone)break;
                    if (baseImportQueue.isEmpty()){
                        Thread.sleep(100);
                        continue;
                    }
//                    System.out.println("Inserting user(" + ((User)baseImportQueue.peek()).getId() + ")...");
                    dao.insertUser((User)baseImportQueue.remove());
                } catch (InterruptedException e) {
                    return;
                }
            }
            importDone = true;
            System.out.println("Import done");
        }
    };

    private void stopImport(){
        for(Thread thread : threads)
            thread.interrupt();
        System.out.println("Critical stop");
    }

    public MainLine(Reporter reporter,AzureConnector connector, PostgresDAO dao, DataParser parser, DataValidator validator){
        this.reporter = reporter;
        azureConnector = connector;
        this.dao = dao;
        this.parser = parser;
        this.validator = validator;
    }
    private void sendReport(){
        if(!importDone)reporter.failed("Import is'nt complete case of critical errors");
        if(!azureConnector.sendStringToFile(reporter.getReport(),"lastImportReport.txt"))
            reporter.writeReportToFile("lastImportReport.txt");
    }
    public void startImport(){
        String trigger = azureConnector.getTrigger();
        if(trigger==null){
            sendReport();
            return;}
        config = parser.parseTrigger(trigger);
        if(config!=null) {
            System.out.println("targetFile = "+config.targetFile);
            threads = new Thread[]{new Thread(startDownload), new Thread(startParse),
                    new Thread(startValidation), new Thread(startImport)};
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
