package com.epam.azuredataimporter.validation;

import com.epam.azuredataimporter.ServiceStatus;
import com.epam.azuredataimporter.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class UserValidationService implements ValidationService<User>{
    @Autowired
    private ObjectValidator<User> validator;

    private Thread[] threads;
    private int maxThreads = 3;
    private int maxQueue = 500;
    private ServiceStatus status = ServiceStatus.Ready;

    private Queue<User> usersQueue;
    private Queue<User> resultsQueue;
    private boolean endOfQueue = false;

    public UserValidationService(){}

    public UserValidationService(int maxThreads, int maxQueue){
        this.maxThreads = maxThreads;
        this.maxQueue = maxQueue;
    }

    private Runnable startValidation = new Runnable() {
        @Override
        public void run() {
            while (true){
                try{
                    if(usersQueue.isEmpty() && endOfQueue)break;
                    while (usersQueue.isEmpty() && !endOfQueue)
                        Thread.sleep(10);
                    User nextUser = usersQueue.remove();
                    if(!validator.isValid(nextUser))continue;
                    while(!resultsQueue.offer(nextUser))Thread.sleep(10);
                } catch (InterruptedException e) {
                    return;
                }
            }
            checkDone();
        }
    };
    private void start(){
        status = ServiceStatus.Working;
        threads = new Thread[maxThreads];
        for(int i=0;i<maxThreads;i++) {
            threads[i] = new Thread(startValidation);
            threads[i].setName("ValidatorThread"+i);
            threads[i].start();
        }
    }
    private void checkDone(){
        for(Thread thread : threads)
            if(thread.isAlive())return;
        status = ServiceStatus.Done;
    }

    @Override
    public Queue<User> startAsyncValidation(Queue<User> queue) throws Exception {
        if(status == ServiceStatus.Working)throw new Exception("Service is busy yet");
        if(queue == null)throw new NullPointerException("Input queue is NULL");
        usersQueue = queue;
        resultsQueue = new ArrayBlockingQueue<>(maxQueue);
        endOfQueue = false;
        start();
        return resultsQueue;
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
