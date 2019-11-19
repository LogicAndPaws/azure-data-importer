package com.epam.azuredataimporter.importing;

import com.epam.azuredataimporter.ServiceStatus;
import com.epam.azuredataimporter.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Queue;

public class UserImportService implements ImportService<User>{
    @Autowired
    private DaoImporter<User> importer;

    private ServiceStatus status = ServiceStatus.Ready;

    private Queue<User> usersQueue;
    private boolean endOfQueue = false;

    public UserImportService(){}

    private Runnable startImport = new Runnable() {
        @Override
        public void run() {
            while (true){
                try{
                    if(usersQueue.isEmpty() && endOfQueue)break;
                    while (usersQueue.isEmpty() && !endOfQueue)
                        Thread.sleep(10);
                    importer.insertObject(usersQueue.remove());
                } catch (InterruptedException e) {
                    return;
                }
            }
            status = ServiceStatus.Done;
        }
    };

    private void start(){
        status = ServiceStatus.Working;
        new Thread(startImport,"ImportThread").start();
    }

    @Override
    public void startAsyncImport(Queue<User> queue) throws Exception {
        if(status == ServiceStatus.Working)throw new Exception("Service is busy yet");
        if(queue == null)throw new NullPointerException("Input queue is NULL");
        usersQueue = queue;
        endOfQueue = false;
        start();
    }

    @Override
    public void endAsyncValidation() {
        endOfQueue = true;
    }

    @Override
    public ServiceStatus getStatus() {
        return status;
    }
}
