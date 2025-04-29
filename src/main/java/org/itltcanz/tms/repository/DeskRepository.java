package org.itltcanz.tms.repository;

import lombok.NonNull;
import org.itltcanz.tms.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeskRepository extends JpaRepository<BoardEntity, Integer> {
    @NonNull
    Page<BoardEntity> findAll(@NonNull Pageable pageable);
    Page<BoardEntity> findDesksByUsersId(Integer userId, Pageable pageable);
}
