package com.epam.azuredataimporter.sources;


import com.epam.azuredataimporter.ResultsObserver;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;

import java.io.*;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

public class AzureConnector implements FileSource, TriggerSource {
    private final String storageConnectionString = "UseDevelopmentStorage=true;";
    private CloudBlobContainer container=null;
    private ResultsObserver observer;
    private String triggerFilename;
    public AzureConnector(ResultsObserver observer, String blobName, String triggerFilename){
        this.observer = observer;
        this.triggerFilename = triggerFilename;
        try {
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
            container = blobClient.getContainerReference(blobName);
            container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());
        } catch (StorageException | URISyntaxException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

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
    @Override
    public boolean sendFile(File file){
        try{
            CloudBlockBlob blob = container.getBlockBlobReference(file.getName());
            blob.uploadFromFile(file.getAbsolutePath());
            return true;
        } catch (StorageException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean sendStringToFile(String stringLine, String filename){
        try {
            CloudBlockBlob blob = container.getBlockBlobReference(filename);
            blob.uploadText(stringLine);
            return true;
        } catch (URISyntaxException | StorageException | IOException e) {
            e.printStackTrace();
            observer.failed("(Critical) Connection with Azure failed with cause:\n"+e.getMessage());
        }
        return false;
    }
    @Override
    public String getTrigger(){
        try {
            CloudBlockBlob blob = container.getBlockBlobReference(triggerFilename);
            return blob.downloadText();
        } catch (StorageException e) {
            observer.failed("(Critical) Cannot download trigger("+e.getMessage()+")");
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void deleteTrigger(){
        try {
            CloudBlockBlob blob = container.getBlockBlobReference(triggerFilename);
            if(!blob.deleteIfExists())
                observer.failed("(Azure) Cannot delete trigger(does'nt exist)");
        } catch (URISyntaxException | StorageException e) {
            e.printStackTrace();
        }

    }
}
