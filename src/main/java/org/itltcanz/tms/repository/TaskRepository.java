package org.itltcanz.tms.repository;

import lombok.NonNull;
import org.itltcanz.tms.entity.UserEntity;
import org.itltcanz.tms.entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {
    @NonNull
    Page<TaskEntity> findAll(Specification<TaskEntity> spec, @NonNull Pageable pageable);
    Page<TaskEntity> findTasksByExecutor(UserEntity executor, Specification<TaskEntity> spec, Pageable pageable);
}
