package com.example.DemoTCP;

import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class TcpConnectionThreadPool {
    private ThreadPoolExecutor executor;

    public TcpConnectionThreadPool() {
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
    }

    public <V> void submit(Callable<V> callable){
        executor.submit(callable);
    }

    public void submit(Runnable runnable){
        executor.submit(runnable);
    }
}
