package exercise.controller;

import java.util.List;

import exercise.dto.TaskCreateDTO;
import exercise.dto.TaskDTO;
import exercise.dto.TaskUpdateDTO;
import exercise.mapper.TaskMapper;
import exercise.model.Task;
import exercise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.TaskRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TasksController {
    // BEGIN
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public List<TaskDTO> getTasks(){
        var tasks = taskRepository.findAll();
        return tasks.stream()
                .map(p -> taskMapper.map(p))
                .toList();
    }

    @GetMapping("/{id}")
    public TaskDTO getTask(@PathVariable long id){
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with Id " + id + " not found"));
        return taskMapper.map(task);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO createTask(@RequestBody TaskCreateDTO taskCreateDTO){
        var task =taskMapper.map(taskCreateDTO);
        var result = taskRepository.save(task);
        return taskMapper.map(result);
    }

    @PutMapping("/{id}")
    public TaskDTO updateTask(@PathVariable long id, @RequestBody TaskUpdateDTO taskUpdateDTO){
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
        taskMapper.update(taskUpdateDTO, task);
        var user = userRepository.findById(taskUpdateDTO.getAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
        task.setAssignee(user);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        taskRepository.deleteById(id);
    }
    // END
}
