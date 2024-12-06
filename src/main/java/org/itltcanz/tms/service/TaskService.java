package org.itltcanz.tms.service;

import lombok.AllArgsConstructor;
import org.itltcanz.tms.entity.Task;
import org.itltcanz.tms.exceptions.EntityException;
import org.itltcanz.tms.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final CommentService commentService;

    public void save(Task task) {
        task.getComments().forEach(commentService::save);
        taskRepository.save(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Integer id) {
        return taskRepository.findById(id).orElseThrow(() -> new EntityException("Task not found"));
    }

    public Task update(Integer id, Task updatedTask) {
        var task = findById(id);
        updatedTask.getComments().forEach(commentService::save);
        updatedTask.setId(id);
        save(updatedTask);
        task.getComments().forEach(commentService::delete);
        return findById(id);
    }

    public void delete(Integer id) {
        Task task = findById(id);
        taskRepository.delete(task);
        task.getComments().forEach(commentService::delete);
    }
}
