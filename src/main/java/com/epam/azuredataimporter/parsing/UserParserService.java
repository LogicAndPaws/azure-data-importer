package com.epam.azuredataimporter.parsing;

import com.epam.azuredataimporter.ServiceStatus;
import com.epam.azuredataimporter.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

@Component
public class UserParserService implements ParseService<User> {
    @Autowired
    private ObjectParser<User> parser;

    private Thread[] threads;
    private int maxThreads = 1;
    private int maxQueue = 500;
    private ServiceStatus status = ServiceStatus.Ready;

    private Queue<String> stringsQueue;
    private Queue<User> resultsQueue;
    private boolean endOfQueue = false;

    //TODO replace to something better than Runnable
    private Runnable startParse = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    if (stringsQueue.isEmpty() && endOfQueue)
                        break;
                    if (stringsQueue.isEmpty()) {
                        continue;
                    }
                    User nextUser = parser.parse(stringsQueue.remove(), User.class);
                    if (nextUser == null) continue;
                    while (!resultsQueue.offer(nextUser)) Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            checkDone();
        }
    };

    public UserParserService() {
    }

    public UserParserService(int maxThreads, int maxQueue) {
        this.maxThreads = maxThreads;
        this.maxQueue = maxQueue;
    }

    private void start() {
        status = ServiceStatus.Working;
        threads = new Thread[maxThreads];
        for (int i = 0; i < maxThreads; i++) {
            threads[i] = new Thread(startParse);
            threads[i].setName("ParserThread" + i);
            threads[i].start();
        }
    }

    private synchronized void checkDone() {
        for (Thread thread : threads)
            if (thread.isAlive() && !Thread.currentThread().equals(thread)) return;
        status = ServiceStatus.Done;
    }

    @Override
    public Queue<User> startAsyncParse(Queue<String> lines) throws Exception {
        if (status == ServiceStatus.Working) throw new Exception("Service is busy yet");
        if (lines == null) throw new NullPointerException("Input queue is NULL");
        stringsQueue = lines;
        resultsQueue = new ArrayBlockingQueue<>(maxQueue);
        endOfQueue = false;
        start();
        return resultsQueue;
    }

    @Override
    public void endAsyncParse() {
        endOfQueue = true;
    }

    @Override
    public boolean isDone() {
        return status == ServiceStatus.Done;
    }
}
