package org.itltcanz.tms.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.itltcanz.tms.dto.task.TaskInDto;
import org.itltcanz.tms.dto.task.TaskOutDto;
import org.itltcanz.tms.entity.UserEntity;
import org.itltcanz.tms.entity.CommentEntity;
import org.itltcanz.tms.entity.TaskEntity;
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
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final CommentService commentService;
    private final StatusService statusService;
    private final PriorityService priorityService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    // Controllers methods
    public TaskOutDto createTask(TaskInDto taskInDto) {
        log.info("Creating task {}", taskInDto);
        var unsavedTaskEntity = modelMapper.map(taskInDto, TaskEntity.class);
        unsavedTaskEntity.setAuthor(userService.getCurrentUser());
        var savedTaskEntity = saveWithComments(unsavedTaskEntity);
        return modelMapper.map(savedTaskEntity, TaskOutDto.class);
    }

    @Cacheable(value = "tasks", key = "#taskId", unless = "#result == null")
    public TaskOutDto getTaskById(Integer taskId) {
        log.info("Retrieving task {}", taskId);
        var currentUser = userService.getCurrentUser();
        TaskEntity taskEntity;
        if (userService.isAdmin(currentUser)) {
            taskEntity = findByIdOrThrow(taskId);
        } else {
            taskEntity = findTaskByIdAndUser(taskId, currentUser);
        }
        return modelMapper.map(taskEntity, TaskOutDto.class);
    }

    public Page<TaskOutDto> getTasks(Pageable pageable, HashMap<String, String> filters) {
        log.info("Retrieving tasks {}", filters);
        // Используем Specification для динамического фильтра
        Specification<TaskEntity> spec = Specification.where(null);

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

        var currentUser = userService.getCurrentUser();
        Page<TaskEntity> taskEntities;

        if (userService.isAdmin(currentUser)) {
            taskEntities = taskRepository.findAll(spec, pageable);
        } else {
            taskEntities = taskRepository.findTasksByExecutor(currentUser, spec, pageable);
        }

        return taskEntities.map(taskEntityEntity -> modelMapper.map(taskEntityEntity, TaskOutDto.class));
    }

    @CacheEvict(value = "tasks", key = "#taskId")
    public TaskOutDto update(Integer taskId, TaskInDto taskInDto) {
        log.info("Updating task {}", taskId);
        var oldTaskEntity = findByIdOrThrow(taskId);
        var newTaskEntity = modelMapper.map(taskInDto, TaskEntity.class);
        newTaskEntity.getComments().forEach(commentService::save);
        newTaskEntity.setId(taskId);
        var taskEntity = saveWithComments(newTaskEntity);
        oldTaskEntity.getComments().forEach(commentService::delete);
        return modelMapper.map(taskEntity, TaskOutDto.class);
    }

    @CacheEvict(value = "tasks", key = "#taskId")
    public void delete(Integer taskId) {
        log.info("Deleting task {}", taskId);
        var taskEntity = findByIdOrThrow(taskId);
        taskRepository.delete(taskEntity);
        taskEntity.getComments().forEach(commentService::delete);
    }

    @CacheEvict(value = "tasks", key = "#taskId")
    public TaskOutDto updateExecutor(Integer taskId, Integer executorId) {
        log.info("Updating executor of task {}", taskId);
        var taskEntity = findByIdOrThrow(taskId);
        var executor = userService.findById(executorId);
        taskEntity.setExecutor(executor);
        return modelMapper.map(saveWithComments(taskEntity), TaskOutDto.class);
    }

    @CacheEvict(value = "tasks", key = "#taskId")
    public TaskOutDto updateStatus(Integer taskId, Integer statusId) {
        log.info("Updating status of task {}", taskId);
        var currentUser = userService.getCurrentUser();
        var taskEntity = findByIdOrThrow(taskId);
        if (userService.isAdmin(currentUser) || taskEntity.getExecutor().equals(currentUser)) {
            var status = statusService.findById(statusId);
            taskEntity.setStatus(status);
            return modelMapper.map(saveWithComments(taskEntity), TaskOutDto.class);
        } else {
            throw new AccessDeniedException("You do not have permission to access this task");
        }
    }

    @CacheEvict(value = "tasks", key = "#taskId")
    public TaskOutDto updatePriority(Integer taskId, Integer priorityId) {
        log.info("Updating priority of task {}", taskId);
        var currentUser = userService.getCurrentUser();
        var taskEntity = findByIdOrThrow(taskId);
        if (userService.isAdmin(currentUser) || taskEntity.getExecutor().equals(currentUser)) {
            var priority = priorityService.findById(priorityId);
            taskEntity.setPriority(priority);
            return modelMapper.map(saveWithComments(taskEntity), TaskOutDto.class);
        } else {
            throw new AccessDeniedException("You do not have permission to access this task");
        }
    }

    @CacheEvict(value = "tasks", key = "#taskId")
    public TaskOutDto addComment(Integer taskId, String text) {
        log.info("Adding comment to task {}", taskId);
        var currentUser = userService.getCurrentUser();
        var taskEntity = findByIdOrThrow(taskId);
        if (userService.isAdmin(currentUser) || taskEntity.getExecutor().equals(currentUser)) {
            var comment = new CommentEntity(currentUser, text);
            commentService.save(comment);
            taskEntity.getComments().add(comment);
            return modelMapper.map(saveWithComments(taskEntity), TaskOutDto.class);
        } else {
            throw new AccessDeniedException("You do not have permission to access this task");
        }
    }

    @CacheEvict(value = "tasks", key = "#taskId")
    public TaskOutDto updateComment(Integer taskId, Integer commentId, String text) {
        log.info("Updating comment to task {}", taskId);
        var commentEntity = commentService.findById(commentId);
        commentEntity.setText(text);
        commentService.save(commentEntity);
        return modelMapper.map(findByIdOrThrow(taskId), TaskOutDto.class);
    }

    @CacheEvict(value = "tasks", key = "#taskId")
    public void deleteComment(Integer taskId, Integer commentId) {
        log.info("Deleting comment to task {}", taskId);
        var task = findByIdOrThrow(taskId);
        var comment = commentService.findById(commentId);
        task.getComments().remove(comment);
        saveWithComments(task);
        commentService.delete(comment);
    }

    // Internal methods

    public TaskEntity findByIdOrThrow(Integer id) {
        return taskRepository.findById(id).orElseThrow(() -> new EntityException("Task not found"));
    }

    public TaskEntity saveWithComments(TaskEntity taskEntity) {
        for (CommentEntity commentEntity : taskEntity.getComments()) {
            commentEntity.setTask(taskEntity);
            commentService.save(commentEntity);
        }
        return taskRepository.save(taskEntity);
    }

    public TaskEntity findTaskByIdAndUser(Integer id, UserEntity userEntity) {
        var task = findByIdOrThrow(id);
        if (!task.getExecutor().equals(userEntity)) {
            throw new AccessDeniedException("You do not have permission to access this task");
        }
        return task;
    }
}
