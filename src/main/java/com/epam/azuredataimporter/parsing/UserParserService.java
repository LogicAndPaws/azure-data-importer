package com.epam.azuredataimporter.parsing;

import com.epam.azuredataimporter.ServiceStatus;
import com.epam.azuredataimporter.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class UserParserService implements ParseService<User>{
    @Autowired
    private ObjectParser<User> parser;
    
    private Thread[] threads;
    private int maxThreads = 3;
    private int maxQueue = 500;
    private ServiceStatus status = ServiceStatus.Ready;

    private Queue<String> stringsQueue;
    private Queue<User> resultsQueue;
    private boolean endOfQueue = false;

    public UserParserService(){}

    public UserParserService(int maxThreads, int maxQueue){
        this.maxThreads = maxThreads;
        this.maxQueue = maxQueue;
    }

    private Runnable startParse = new Runnable() {
        @Override
        public void run() {
            while (true){
                try{
                    if(stringsQueue.isEmpty() && endOfQueue)break;
                    while (stringsQueue.isEmpty() && !endOfQueue)
                        Thread.sleep(10);
                    User nextUser = parser.parse(stringsQueue.remove());
                    if(nextUser==null)continue;
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
            threads[i] = new Thread(startParse);
            threads[i].setName("ParserThread"+i);
            threads[i].start();
        }
    }
    private void checkDone(){
        for(Thread thread : threads)
            if(thread.isAlive())return;
        status = ServiceStatus.Done;
    }
    @Override
    public Queue<User> startAsyncParse(Queue<String> lines) throws Exception{
        if(status == ServiceStatus.Working)throw new Exception("Service is busy yet");
        if(lines == null)throw new NullPointerException("Input queue is NULL");
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
    public ServiceStatus getStatus() {
        return status;
    }

}
