package com.epam.azuredataimporter.validation;

import com.epam.azuredataimporter.ServiceStatus;
import com.epam.azuredataimporter.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

@Component
public class UserValidationService implements ValidationService<User> {
    @Autowired
    private ObjectValidator<User> validator;

    private Thread[] threads;
    private int maxThreads = 1;
    private int maxQueue = 500;
    private ServiceStatus status = ServiceStatus.Ready;

    private Queue<User> usersQueue;
    private Queue<User> resultsQueue;
    private boolean endOfQueue = false;

    //TODO replace to something better than Runnable
    private Runnable startValidation = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    if (usersQueue.isEmpty() && endOfQueue)
                        break;
                    if (usersQueue.isEmpty()) {
                        continue;
                    }
                    User nextUser = usersQueue.remove();
                    if (!validator.isValid(nextUser)) continue;
                    while (!resultsQueue.offer(nextUser)) Thread.sleep(20);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            checkDone();
        }
    };

    private void start() {
        status = ServiceStatus.Working;
        threads = new Thread[maxThreads];
        for (int i = 0; i < maxThreads; i++) {
            threads[i] = new Thread(startValidation);
            threads[i].setName("ValidatorThread" + i);
            threads[i].start();
        }
    }

    private synchronized void checkDone() {
        for (Thread thread : threads)
            if (thread.isAlive() && !Thread.currentThread().equals(thread)) return;
        status = ServiceStatus.Done;
    }

    @Override
    public Queue<User> startAsyncValidation(Queue<User> queue) throws Exception {
        if (status == ServiceStatus.Working) throw new Exception("Service is busy yet");
        if (queue == null) {
            throw new NullPointerException("Input queue is NULL");
        }
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
    public boolean isDone() {
        return status == ServiceStatus.Done;
    }
}
