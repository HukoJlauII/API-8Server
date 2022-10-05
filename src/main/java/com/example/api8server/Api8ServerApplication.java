package com.example.api8server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication

public class Api8ServerApplication {
    private static TaskRepository taskRepository;
    @Autowired
    public static void setTaskRepository(TaskRepository taskRepository) {
        Api8ServerApplication.taskRepository = taskRepository;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(Api8ServerApplication.class, args);
        Server server = ServerBuilder.forPort(8081).addService(new TaskServiceImpl(taskRepository)).build();

        server.start();

        System.out.println("Server started");
        server.awaitTermination();
    }


}
