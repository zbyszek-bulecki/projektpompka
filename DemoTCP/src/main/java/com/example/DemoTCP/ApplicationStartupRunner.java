package com.example.DemoTCP;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupRunner implements ApplicationRunner {
    private TcpConnectionsListener server;

    public ApplicationStartupRunner(TcpConnectionsListener server) {
        this.server = server;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        server.start(1234);
    }
}
