package balabol.error.free.text.repositories;

import balabol.error.free.text.entities.Task;
import balabol.error.free.text.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query("""
        SELECT t FROM Task t
        WHERE t.status = :status
        ORDER BY t.id
        LIMIT 1
    """)
    Optional<Task> findFirstByStatus(@Param("status") TaskStatus status);

    @Modifying
    @Query("UPDATE Task t SET t.status = :status WHERE t = :task")
    void updateStatus(@Param("task") Task task, @Param("status") TaskStatus status);

    @Modifying
    @Query("UPDATE Task t SET t.text = :text, t.status = :status WHERE t = :task")
    void updateTextAndStatus(@Param("task") Task task,
                             @Param("text") String text,
                             @Param("status") TaskStatus status);

    @Modifying
    @Query("UPDATE Task t SET t.status = :status, t.errorMessage = :errorMessage WHERE t = :task")
    void updateStatusAndErrorMessage(@Param("task") Task task,
                                     @Param("status") TaskStatus status,
                                     @Param("errorMessage") String errorMessage);
}
