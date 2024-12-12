package org.itltcanz.tms.service;

import lombok.AllArgsConstructor;
import org.itltcanz.tms.entity.Comment;
import org.itltcanz.tms.exceptions.EntityException;
import org.itltcanz.tms.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public Comment findById(Integer id) {
        return commentRepository.findById(id).orElseThrow(() -> new EntityException("Comment not found"));
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

}
