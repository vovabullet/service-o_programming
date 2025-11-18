package grpc.demo;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import grpc.demo.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@GrpcService // Аннотация, которая регистрирует этот класс как gRPC сервис
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    private final ConcurrentMap<String, User> users = new ConcurrentHashMap<>();

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<CreateUserResponse> responseObserver) {
        // Создаем нового пользователя с уникальным ID
        User newUser = User.newBuilder()
                .setUserId(UUID.randomUUID().toString())
                .setName(request.getName())
                .build();
        users.put(newUser.getUserId(), newUser);

        System.out.println("Создан пользователь: " + newUser.getName());

        // Создаем и отправляем ответ
        CreateUserResponse response = CreateUserResponse.newBuilder().setUser(newUser).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserBadges(GetUserBadgesRequest request, StreamObserver<GetUserBadgesResponse> responseObserver) {
        String userId = request.getUserId();

        // Имитируем получение достижений для пользователя
        System.out.println("Запрошены достижения для пользователя: " + userId);
        Badge badge1 = Badge.newBuilder()
                .setBadgeId("b1")
                .setName("Первый шаг")
                .setDescription("Успешно создан аккаунт")
                .build();
        Badge badge2 = Badge.newBuilder()
                .setBadgeId("b2")
                .setName("Любопытный")
                .setDescription("Запросил свои достижения")
                .build();

        // Создаем и отправляем ответ со списком достижений
        GetUserBadgesResponse response = GetUserBadgesResponse.newBuilder()
                .addAllBadges(List.of(badge1, badge2))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
