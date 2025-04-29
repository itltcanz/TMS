package org.itltcanz.tms.service;

import lombok.AllArgsConstructor;
import org.itltcanz.tms.entity.CommentEntity;
import org.itltcanz.tms.exceptions.EntityException;
import org.itltcanz.tms.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    public void save(CommentEntity commentEntity) {
        commentRepository.save(commentEntity);
    }

    public CommentEntity findById(Integer id) {
        return commentRepository.findById(id).orElseThrow(() -> new EntityException("Comment not found"));
    }

    public void delete(CommentEntity commentEntity) {
        commentRepository.delete(commentEntity);
    }

}
