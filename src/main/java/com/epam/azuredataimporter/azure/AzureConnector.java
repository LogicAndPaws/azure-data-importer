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
    public AzureConnector(ResultsObserver observer,String blobName){
        this.observer = observer;
        try {
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
            container = blobClient.getContainerReference(blobName);
            container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());
        } catch (StorageException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
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
    public BufferedReader openReadStream(String filename){
        try {
            CloudBlockBlob blob = container.getBlockBlobReference(filename);
            BlobInputStream blobInputStream = blob.openInputStream();
            return new BufferedReader(new InputStreamReader(blobInputStream));
        } catch (StorageException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean sendFile(File file){
        try{
            CloudBlockBlob blob = container.getBlockBlobReference(file.getName());
            blob.uploadFromFile(file.getAbsolutePath());
            return true;
        } catch (StorageException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean sendStringToFile(String stringLine, String filename){
        try {
            CloudBlockBlob blob = container.getBlockBlobReference(filename);
            blob.uploadText(stringLine);
            return true;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (StorageException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
