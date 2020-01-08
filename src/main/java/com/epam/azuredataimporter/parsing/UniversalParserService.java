package com.epam.azuredataimporter.parsing;

import com.epam.azuredataimporter.ServiceStatus;
import com.epam.azuredataimporter.entity.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

@Component
public class UniversalParserService<T extends Entity> implements ParseService<T> {
    @Autowired
    private ObjectParser<T> parser;
    private Class<T> clazz;

    private Thread[] threads;
    private int maxThreads = 1;
    private int maxQueue = 500;
    private ServiceStatus status = ServiceStatus.Ready;

    private Queue<String> stringsQueue;
    private Queue<T> resultsQueue;
    private boolean endOfQueue = false;

    //TODO replace to something better then Runnable
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
                    T nextEntity = parser.parse(stringsQueue.remove(), clazz);
                    if (nextEntity == null) continue;
                    while (!resultsQueue.offer(nextEntity)) Thread.sleep(10);
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
    public Queue<T> startAsyncParse(Queue<String> lines, Class<T> clazz) throws Exception {
        if (status == ServiceStatus.Working) throw new Exception("Service is busy yet");
        if (lines == null) throw new NullPointerException("Input queue is NULL");
        stringsQueue = lines;
        resultsQueue = new ArrayBlockingQueue<>(maxQueue);
        endOfQueue = false;
        this.clazz = clazz;
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
