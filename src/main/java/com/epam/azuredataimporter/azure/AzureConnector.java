package com.epam.azuredataimporter.azure;


import com.epam.azuredataimporter.ResultsObserver;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;

import java.io.*;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

public class AzureConnector {
    private final String storageConnectionString = "UseDevelopmentStorage=true;";
    private CloudBlobContainer container=null;
    private ResultsObserver observer;
    private String triggerFilename;
    public AzureConnector(ResultsObserver observer,String blobName,String triggerFilename){
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
//    public BufferedReader openReadStream(String filename){
//        try {
//            CloudBlockBlob blob = container.getBlockBlobReference(filename);
//            BlobInputStream blobInputStream = blob.openInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(blobInputStream));
//            System.out.println(reader.ready());
//            System.out.println(reader.readLine());
//            System.out.println(reader.readLine());
//            return reader;
////            return new BufferedReader(new InputStreamReader(blobInputStream));
//        } catch (StorageException | URISyntaxException | IOException e) {
//            e.printStackTrace();
//            observer.failed("(Critical) Connection with Azure failed with cause:\n"+e.getMessage());
//            return null;
//        }
//    }
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
