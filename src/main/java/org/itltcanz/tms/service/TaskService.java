package org.itltcanz.tms.service;

import lombok.AllArgsConstructor;
import org.itltcanz.tms.entity.Comment;
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
    private final StatusService statusService;
    private final PriorityService priorityService;

    public Task save(Task task) {
        task.getComments().forEach(commentService::save);
        taskRepository.save(task);
        return findById(task.getId());
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
        var task = findById(id);
        taskRepository.delete(task);
        task.getComments().forEach(commentService::delete);
    }

    public Task updateStatus(Integer taskId, Integer statusId) {
        var task = findById(taskId);
        var status = statusService.findById(statusId);
        task.setStatus(status);
        return save(task);
    }

    public Task updatePriority(Integer taskId, Integer priorityId) {
        var task = findById(taskId);
        var priority = priorityService.findById(priorityId);
        task.setPriority(priority);
        return save(task);
    }

    public Task addComment(Integer taskId, Comment comment) {
        var task = findById(taskId);
        commentService.save(comment);
        task.getComments().add(comment);
        return save(task);
    }
}
