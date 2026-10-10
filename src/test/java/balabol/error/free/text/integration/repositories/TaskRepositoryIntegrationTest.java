package balabol.error.free.text.integration.repositories;

import balabol.error.free.text.entities.Task;
import balabol.error.free.text.enums.LanguageType;
import balabol.error.free.text.enums.TaskStatus;
import balabol.error.free.text.repositories.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

@DisplayName("integration tests for TaskRepository")
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers(disabledWithoutDocker = true)
public class TaskRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> POSTGRES_DB
            = new PostgreSQLContainer<>("postgres:17.0");

    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("When method connectionEstablishToDatabase " +
            "Then true and true")
    void connectionEstablishToDatabase_trueAndTrue() {
        Assertions.assertTrue(POSTGRES_DB.isCreated());
        Assertions.assertTrue(POSTGRES_DB.isRunning());
    }

    @Test
    @DisplayName("Given there are tasks with status NEW " +
            "When method findFirstByStatus Then optional task")
    void thereAreTasksWithStatusNew_findFirstByStatus_optionalTask() {

        Task expectedTask = new Task("text", LanguageType.RU, TaskStatus.NEW);

        Optional<Task> expectedOptionalTask =
                Optional.of(taskRepository.save(expectedTask));

        Optional<Task> actualOptionalTask =
                taskRepository.findFirstByStatus(TaskStatus.NEW);

        Assertions.assertEquals(expectedOptionalTask, actualOptionalTask);
    }

    @Test
    @DisplayName("Given there are no tasks with status NEW " +
            "When method findFirstByStatus Then optional empty")
    void findFirstByStatus_optionalEmpty() {

        Optional<Task> actualOptionalTask =
                taskRepository.findFirstByStatus(TaskStatus.NEW);

        Assertions.assertEquals(Optional.empty(), actualOptionalTask);
    }

    @Test
    @DisplayName("When method updateTextAndStatus " +
            "Then number modified lines equal one")
    void updateTextAndStatus_numberModifiedLinesEqualOne() {

        Task taskForUpdate =
                taskRepository.save(new Task("text", LanguageType.RU, TaskStatus.NEW));

        int actualNumberModifiedLines = taskRepository.updateTextAndStatus(taskForUpdate,
                "new text", TaskStatus.COMPLECTED);

        Assertions.assertEquals(1, actualNumberModifiedLines);
    }

    @Test
    @DisplayName("When method updateStatusAndErrorMessage " +
            "Then number modified lines equal one")
    void updateStatusAndErrorMessage_numberModifiedLinesEqualOne() {

        Task taskForUpdate =
                taskRepository.save(new Task("text", LanguageType.RU, TaskStatus.NEW));

        int actualNumberModifiedLines = taskRepository.updateStatusAndErrorMessage(taskForUpdate,
                TaskStatus.COMPLETED_WITH_ERROR, "error message");

        Assertions.assertEquals(1, actualNumberModifiedLines);
    }
}
