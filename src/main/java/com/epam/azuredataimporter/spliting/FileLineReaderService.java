package com.epam.azuredataimporter.spliting;

import com.epam.azuredataimporter.ServiceStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class FileLineReaderService implements LineReaderService {

    ServiceStatus status = ServiceStatus.Ready;
    BufferedReader reader;

    private Queue<String> linesQueue;
    private int maxQueue = 500;

    public FileLineReaderService(){}

    public FileLineReaderService(int maxQueue){
        this.maxQueue = maxQueue;
    }

    private Runnable startRead = new Runnable() {
        @Override
        public void run() {
            while(true){
                try{
                    while(linesQueue.size()>=maxQueue-1)Thread.sleep(20);
                    if(reader.ready())
                    linesQueue.offer(reader.readLine());
                    else break;
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            status = ServiceStatus.Done;
        }
    };

    private void start(){
        status = ServiceStatus.Working;
        new Thread(startRead,"ImportThread").start();
    }

    @Override
    public Queue<String> splitStream(InputStream stream) throws Exception {
        if(status == ServiceStatus.Working)throw new Exception("Service is busy yet");
        if(stream == null)throw new NullPointerException("Input stream is NULL");
        reader = new BufferedReader(new InputStreamReader(stream));
        linesQueue = new ArrayBlockingQueue<>(maxQueue);
        start();
        return linesQueue;
    }

    @Override
    public ServiceStatus getStatus() {
        return status;
    }
}
