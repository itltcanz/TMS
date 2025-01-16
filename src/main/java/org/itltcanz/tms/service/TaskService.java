package org.itltcanz.tms.service;

import lombok.AllArgsConstructor;
import org.itltcanz.tms.dto.task.TaskInDto;
import org.itltcanz.tms.dto.task.TaskOutDto;
import org.itltcanz.tms.entity.Account;
import org.itltcanz.tms.entity.Comment;
import org.itltcanz.tms.entity.Task;
import org.itltcanz.tms.exceptions.EntityException;
import org.itltcanz.tms.repository.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final CommentService commentService;
    private final StatusService statusService;
    private final PriorityService priorityService;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    // Controllers methods
    public TaskOutDto createTask(TaskInDto taskInDto) {
        var taskEntity = modelMapper.map(taskInDto, Task.class);
        taskEntity.setAuthor(accountService.getCurrentUser());
        if (taskEntity.getComments() != null) {
            taskEntity.getComments().forEach(commentService::save);
        }
        taskRepository.save(taskEntity);
        return modelMapper.map(findById(taskEntity.getId()), TaskOutDto.class) ;
    }

    @Cacheable(value = "tasks", key = "#taskId", unless = "#result == null")
    public TaskOutDto getTaskById(Integer taskId) {
        var account = accountService.getCurrentUser();
        Task taskEntity;
        if (accountService.isAdmin(account)) {
            taskEntity = findById(taskId);
        } else {
            taskEntity = findTaskByIdAndAccount(taskId, account);
        }
        return modelMapper.map(taskEntity, TaskOutDto.class);
    }

    public Page<TaskOutDto> getTasks(Pageable pageable, HashMap<String, String> filters) {
        // Используем Specification для динамического фильтра
        Specification<Task> spec = Specification.where(null);

        // Создаем новое значение спецификации на каждом шаге
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.equals("title")) {
                spec = spec.and((root, query, cb) -> cb.like(root.get(key), "%" + value + "%"));
            } else {
                spec = spec.and((root, query, cb) -> cb.equal(root.get(key).get("id"), value));
            }
        }

        var account = accountService.getCurrentUser();
        Page<Task> taskEntities;

        if (accountService.isAdmin(account)) {
            taskEntities = taskRepository.findAll(spec, pageable);
        } else {
            taskEntities = taskRepository.findTasksByExecutor(account, spec, pageable);
        }

        return taskEntities.map(taskEntity -> modelMapper.map(taskEntity, TaskOutDto.class));
    }

    @CacheEvict(value = "tasks", key = "#taskId")
    public TaskOutDto update(Integer taskId, TaskInDto taskInDto) {
        var oldTaskEntity = findById(taskId);
        var newTaskEntity = modelMapper.map(taskInDto, Task.class);
        newTaskEntity.getComments().forEach(commentService::save);
        newTaskEntity.setId(taskId);
        save(newTaskEntity);
        oldTaskEntity.getComments().forEach(commentService::delete);
        return modelMapper.map(findById(newTaskEntity.getId()), TaskOutDto.class) ;
    }

    @CacheEvict(value = "tasks", key = "#taskId")
    public void delete(Integer taskId) {
        var taskEntity = findById(taskId);
        taskRepository.delete(taskEntity);
        taskEntity.getComments().forEach(commentService::delete);
    }

    @CacheEvict(value = "tasks", key = "#taskId")
    public TaskOutDto updateExecutor(Integer taskId, Integer executorId) {
        var taskEntity = findById(taskId);
        var executor = accountService.findById(executorId);
        taskEntity.setExecutor(executor);
        return modelMapper.map(save(taskEntity), TaskOutDto.class);
    }

    @CacheEvict(value = "tasks", key = "#taskId")
    public TaskOutDto updateStatus(Integer taskId, Integer statusId) {
        var account = accountService.getCurrentUser();
        var taskEntity = findById(taskId);
        if (accountService.isAdmin(account) || taskEntity.getExecutor().equals(account)) {
            var status = statusService.findById(statusId);
            taskEntity.setStatus(status);
            return modelMapper.map(save(taskEntity), TaskOutDto.class);
        } else {
            throw new AccessDeniedException("You do not have permission to access this task");
        }
    }

    @CacheEvict(value = "tasks", key = "#taskId")
    public TaskOutDto updatePriority(Integer taskId, Integer priorityId) {
        var account = accountService.getCurrentUser();
        var taskEntity = findById(taskId);
        if (accountService.isAdmin(account) || taskEntity.getExecutor().equals(account)) {
            var priority = priorityService.findById(priorityId);
            taskEntity.setPriority(priority);
            return modelMapper.map(save(taskEntity), TaskOutDto.class);
        } else {
            throw new AccessDeniedException("You do not have permission to access this task");
        }
    }

    @CacheEvict(value = "tasks", key = "#taskId")
    public TaskOutDto addComment(Integer taskId, String text) {
        var account = accountService.getCurrentUser();
        var taskEntity = findById(taskId);
        if (accountService.isAdmin(account) || taskEntity.getExecutor().equals(account)) {
            var comment = new Comment(account, text);
            commentService.save(comment);
            taskEntity.getComments().add(comment);
            return modelMapper.map(save(taskEntity), TaskOutDto.class);
        } else {
            throw new AccessDeniedException("You do not have permission to access this task");
        }
    }

    @CacheEvict(value = "tasks", key = "#taskId")
    public TaskOutDto updateComment(Integer taskId, Integer commentId, String text) {
        var commentEntity = commentService.findById(commentId);
        commentEntity.setText(text);
        commentService.save(commentEntity);
        return modelMapper.map(findById(taskId), TaskOutDto.class);
    }

    @CacheEvict(value = "tasks", key = "#taskId")
    public void deleteComment(Integer taskId, Integer commentId) {
        var task = findById(taskId);
        var comment = commentService.findById(commentId);
        task.getComments().remove(comment);
        save(task);
        commentService.delete(comment);
    }

    // Internal methods

    public Task findById(Integer id) {
        return taskRepository.findById(id).orElseThrow(() -> new EntityException("Task not found"));
    }

    public Task save(Task task) {
        task.getComments().forEach(commentService::save);
        taskRepository.save(task);
        return findById(task.getId());
    }

    public Task findTaskByIdAndAccount(Integer id, Account account) {
        var task = taskRepository.findById(id).orElseThrow(() -> new EntityException("Task not found"));
        if (!task.getExecutor().equals(account)) {
            throw new AccessDeniedException("You do not have permission to access this task");
        }
        return task;
    }
}
