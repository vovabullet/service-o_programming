package grpc.demo;

import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import grpc.demo.*;

@Component
public class GrpcClientRunner implements CommandLineRunner {

    // Внедряем заглушку (stub) для нашего gRPC сервиса
    // user-service - имя, которое мы задали в application.properties
    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userServiceStub;

    @Override
    public void run(String... args) {
        System.out.println("--- Клиент gRPC запущен ---");
        try {
            // 1. Создаем пользователя
            System.out.println("--> Вызов CreateUser...");
            CreateUserRequest createRequest = CreateUserRequest.newBuilder().setName("Студент").build();
            CreateUserResponse createResponse = userServiceStub.createUser(createRequest);
            String newUserId = createResponse.getUser().getUserId();
            System.out.println("<-- Пользователь успешно создан с ID: " + newUserId);

            // 2. Получаем его достижения
            System.out.println("\n--> Вызов GetUserBadges для пользователя " + newUserId + "...");
            GetUserBadgesRequest badgesRequest = GetUserBadgesRequest.newBuilder().setUserId(newUserId).build();
            GetUserBadgesResponse badgesResponse = userServiceStub.getUserBadges(badgesRequest);
            System.out.println("<-- Получены достижения:");
            for (Badge badge : badgesResponse.getBadgesList()) {
                System.out.println("    - " + badge.getName() + " (" + badge.getDescription() + ")");
            }

        } catch (StatusRuntimeException e) {
            System.err.println("!!! Ошибка при вызове gRPC: " + e.getStatus());
        }
        System.out.println("--- Клиент gRPC завершил работу ---");
    }
}
