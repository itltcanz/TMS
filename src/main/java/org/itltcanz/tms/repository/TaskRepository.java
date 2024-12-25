package org.itltcanz.tms.repository;

import lombok.NonNull;
import org.itltcanz.tms.entity.Account;
import org.itltcanz.tms.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    @NonNull Page<Task> findAll(Specification<Task> spec, @NonNull Pageable pageable);
    Page<Task> findTasksByExecutor(Account executor, Specification<Task> spec, Pageable pageable);
}
