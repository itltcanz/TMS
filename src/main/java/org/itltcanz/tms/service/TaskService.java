package org.itltcanz.tms.service;

import lombok.AllArgsConstructor;
import org.itltcanz.tms.entity.Account;
import org.itltcanz.tms.entity.Comment;
import org.itltcanz.tms.entity.Task;
import org.itltcanz.tms.exceptions.EntityException;
import org.itltcanz.tms.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final CommentService commentService;
    private final StatusService statusService;
    private final PriorityService priorityService;
    private final AccountService accountService;

    public Task save(Task task) {
        task.getComments().forEach(commentService::save);
        taskRepository.save(task);
        return findById(task.getId());
    }

    public Page<Task> getTasks(Pageable pageable) {
        var account = accountService.getCurrentUser();
        Page<Task> tasks;
        if (accountService.isAdmin(account)) {
            tasks = taskRepository.findAll(pageable);
        } else {
            tasks = taskRepository.findTasksByExecutor(account, pageable);
        }
        return tasks;
    }

    public Task getTaskById(Integer id) {
        var account = accountService.getCurrentUser();
        if (accountService.isAdmin(account)) {
            return findById(id);
        } else {
            return findTaskByIdAndAccount(id, account);
        }
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

    public Task updateExecutor(Integer taskId, Integer executorId) {
        var task = findById(taskId);
        var executor = accountService.findById(executorId);
        task.setExecutor(executor);
        return save(task);
    }

    public Task updateStatus(Integer taskId, Integer statusId) {
        var account = accountService.getCurrentUser();
        var task = findById(taskId);
        if (accountService.isAdmin(account) || task.getExecutor().equals(account)) {
            var status = statusService.findById(statusId);
            task.setStatus(status);
            return save(task);
        } else {
            throw new AccessDeniedException("You do not have permission to access this task");
        }
    }

    public Task updatePriority(Integer taskId, Integer priorityId) {
        var account = accountService.getCurrentUser();
        var task = findById(taskId);
        if (accountService.isAdmin(account) || task.getExecutor().equals(account)) {
            var priority = priorityService.findById(priorityId);
            task.setPriority(priority);
            return save(task);
        } else {
            throw new AccessDeniedException("You do not have permission to access this task");
        }
    }

    public Task addComment(Integer taskId, String text) {
        var account = accountService.getCurrentUser();
        var task = findById(taskId);
        if (accountService.isAdmin(account) || task.getExecutor().equals(account)) {
            var comment = new Comment(account, text);
            commentService.save(comment);
            task.getComments().add(comment);
            return save(task);
        } else {
            throw new AccessDeniedException("You do not have permission to access this task");
        }
    }

    public Task findTaskByIdAndAccount(Integer id, Account account) {
        var task = taskRepository.findById(id).orElseThrow(() -> new EntityException("Task not found"));
        if (!task.getExecutor().equals(account)) {
            throw new AccessDeniedException("You do not have permission to access this task");
        }
        return task;
    }

    public void deleteComment(Integer taskId, Integer commentId) {
        var task = findById(taskId);
        var comment = commentService.findById(commentId);
        task.getComments().remove(comment);
        save(task);
        commentService.delete(comment);
    }
}
