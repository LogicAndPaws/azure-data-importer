package com.epam.azuredataimporter.sources;


import com.epam.azuredataimporter.config.ApplicationConfig;
import com.epam.azuredataimporter.reporting.ReportSender;
import com.epam.azuredataimporter.reporting.ResultsObserver;
import com.epam.azuredataimporter.trigger.TriggerSource;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

@Component
public class AzureConnector implements FileSource, TriggerSource, ReportSender {
    private final String storageConnectionString = "UseDevelopmentStorage=true;";
    private CloudBlobContainer container = null;
    @Autowired
    private ResultsObserver observer;
    private String triggerFilename;

    public AzureConnector(ApplicationConfig config) {
        this.triggerFilename = config.getImportTrigger();
        try {
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
            container = blobClient.getContainerReference(config.getBlobName());
            container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());
        } catch (StorageException | URISyntaxException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    ////////////////////////FileSource/////////////////////
    @Override
    public File readFile(String filename) {
        try {
            File file = new File(filename);
            CloudBlockBlob blob = container.getBlockBlobReference(filename);
            blob.downloadToFile(file.getAbsolutePath());
            return file;
        } catch (StorageException | URISyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    ////////////////////////TriggerSource/////////////////////
    @Override
    public String getTrigger() {
        try {
            CloudBlockBlob blob = container.getBlockBlobReference(triggerFilename);
            return blob.downloadText();
        } catch (StorageException e) {
            observer.failed("(Critical) Cannot download trigger(" + e.getMessage() + ")");
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteTrigger() {
        try {
            CloudBlockBlob blob = container.getBlockBlobReference(triggerFilename);
            if (!blob.deleteIfExists())
                observer.failed("(Azure) Cannot delete trigger(does'nt exist)");
        } catch (URISyntaxException | StorageException e) {
            e.printStackTrace();
        }

    }

    ////////////////////////ReportSender/////////////////////
    @Override
    public boolean sendReport(File reportFile) {
        try {
            CloudBlockBlob blob = container.getBlockBlobReference(reportFile.getName());
            blob.uploadFromFile(reportFile.getAbsolutePath());
            return true;
        } catch (StorageException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean sendReport(String report, String reportName) {
        try {
            CloudBlockBlob blob = container.getBlockBlobReference(reportName);
            blob.uploadText(report);
            return true;
        } catch (URISyntaxException | StorageException | IOException e) {
            e.printStackTrace();
            observer.failed("(Critical) Connection with Azure failed with cause:\n" + e.getMessage());
        }
        return false;
    }
}
