package com.epam.azuredataimporter.daoimporting;

import com.epam.azuredataimporter.ServiceStatus;
import com.epam.azuredataimporter.entity.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Queue;

@Component
public class UniversalBaseImportService<T extends Entity> implements BaseImportService<T> {
    @Autowired
    private BaseImporter<T> importer;

    private Thread[] threads;
    private int maxThreads = 1;
    private ServiceStatus status = ServiceStatus.Ready;

    private Queue<T> entityQueue;
    private boolean endOfQueue = false;

    private Runnable startImport = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    if (entityQueue.isEmpty() && endOfQueue) break;
                    if (entityQueue.isEmpty()) {
                        Thread.sleep(20);
                        continue;
                    }
                    try {
                        importer.insertObject(entityQueue.remove());
                    } catch (NoSuchElementException ignore) {
                    }
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
            threads[i] = new Thread(startImport);
            threads[i].setName("ImporterThread" + i);
            threads[i].start();
        }
    }

    private synchronized void checkDone() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignore) {
            Thread.currentThread().interrupt();
        }
        for (Thread thread : threads)
            if (thread.isAlive() && !Thread.currentThread().equals(thread)) return;
        status = ServiceStatus.Done;
    }

    @Override
    public void startAsyncImport(Queue<T> queue) throws Exception {
        if (status == ServiceStatus.Working) throw new Exception("Service is busy yet");
        if (queue == null) throw new NullPointerException("Input queue is NULL");
        entityQueue = queue;
        endOfQueue = false;
        start();
    }

    @Override
    public void endAsyncImport() {
        endOfQueue = true;
    }

    @Override
    public boolean isDone() {
        return status == ServiceStatus.Done;
    }
}
