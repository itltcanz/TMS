package org.itltcanz.tms.repository;

import org.itltcanz.tms.entity.Account;
import org.itltcanz.tms.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findTasksByExecutor(Account executor);
}
