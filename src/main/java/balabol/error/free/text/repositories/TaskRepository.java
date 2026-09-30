package balabol.error.free.text.repositories;

import balabol.error.free.text.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
