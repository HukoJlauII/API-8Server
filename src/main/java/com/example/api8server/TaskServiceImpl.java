package com.example.api8server;

import com.example.grpc.TaskServiceGrpc;
import com.example.grpc.TaskServiceOuterClass;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;

public class TaskServiceImpl extends TaskServiceGrpc.TaskServiceImplBase {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void addTask(TaskServiceOuterClass.TaskRequest request, StreamObserver<TaskServiceOuterClass.TaskResponse> responseObserver) {
        Task task = taskRepository.save(new Task(request.getName(), request.getDescription(), request.getDone()));
        TaskServiceOuterClass.TaskResponse taskResponse = TaskServiceOuterClass.TaskResponse.newBuilder()
                .setId(task.getId())
                .setName(task.getName())
                .setDescription(task.getDescription())
                .setDone(request.getDone()).build();
        System.out.println("Task " + request.getName() + "added");

        responseObserver.onNext(taskResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteTask(TaskServiceOuterClass.TaskId request, StreamObserver<TaskServiceOuterClass.Success> responseObserver) {
        taskRepository.deleteById(request.getId());
        TaskServiceOuterClass.Success success = TaskServiceOuterClass.Success.newBuilder().setSuccess(true).build();
        System.out.println("Task with id " + request.getId() + " deleted");
        responseObserver.onNext(success);
        responseObserver.onCompleted();
    }

    @Override
    public void allTasks(TaskServiceOuterClass.Success request, StreamObserver<TaskServiceOuterClass.AllTasksResponse> responseObserver) {
        ArrayList<TaskServiceOuterClass.TaskResponse> tasksResponse = new ArrayList<>();
        Iterable<Task> tasks = taskRepository.findAll();
        for (Task task :
                tasks) {
            tasksResponse.add(TaskServiceOuterClass.TaskResponse.newBuilder()
                    .setId(task.getId())
                    .setName(task.getName())
                    .setDescription(task.getDescription())
                    .setDone(task.isDone())
                    .build());
        }
        TaskServiceOuterClass.AllTasksResponse allTasksResponse = TaskServiceOuterClass.AllTasksResponse.newBuilder().addAllCases(tasksResponse).build();
        responseObserver.onNext(allTasksResponse);
        responseObserver.onCompleted();

    }

    @Override
    public void editTask(TaskServiceOuterClass.TaskRequest request, StreamObserver<TaskServiceOuterClass.TaskResponse> responseObserver) {
        Task task = taskRepository.save(new Task(request.getId(), request.getName(), request.getDescription(), request.getDone()));
        TaskServiceOuterClass.TaskResponse taskResponse = TaskServiceOuterClass.TaskResponse.newBuilder()
                .setId(task.getId())
                .setName(task.getName())
                .setDescription(task.getDescription())
                .setDone(request.getDone()).build();
        System.out.println("Task " + request.getName() + "changed");

        responseObserver.onNext(taskResponse);
        responseObserver.onCompleted();
    }


}
