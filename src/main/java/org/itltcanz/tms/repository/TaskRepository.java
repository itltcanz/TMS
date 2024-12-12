package org.itltcanz.tms.repository;

import org.itltcanz.tms.entity.Account;
import org.itltcanz.tms.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;


@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Page<Task> findAll(Pageable pageable);
    Page<Task> findTasksByExecutor(Account executor, Pageable pageable);
}
