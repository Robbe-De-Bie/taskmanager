package be.ucll.taskmanager.controllers;

import be.ucll.taskmanager.domain.DTO.SubtaskDTO;
import be.ucll.taskmanager.domain.DTO.TaskDTO;
import be.ucll.taskmanager.domain.service.TaskServiceImp;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RestTaskController {

    private final TaskServiceImp service;

    public RestTaskController(TaskServiceImp service) {
        this.service = service;
    }
    @GetMapping("/tasks")
    @ResponseBody
    public List<TaskDTO> getTasks(){
        return service.getAllTasks();
    }
    @GetMapping("/tasks/{id}")
    @ResponseBody
    public TaskDTO getTaskDTO(@PathVariable("id") String id){
        return service.getTaskDTO(UUID.fromString(id));
    }
    @PostMapping("/addTask")
    @ResponseBody
    public TaskDTO addTask(@RequestBody @Valid TaskDTO taskDTO) {
        return service.addTask(taskDTO);
    }
    //Method to be executed when you submit the edited task
    @PostMapping("/editTask")
    @ResponseBody
    public TaskDTO editTask(@RequestBody @Valid TaskDTO taskDTO){
        return service.editTask(taskDTO);

    }
    @PostMapping("/addSubtask")
    @ResponseBody
    public SubtaskDTO addSubtask(@RequestParam(value="idMainTask") String id, @RequestBody @Valid SubtaskDTO subtask){
        UUID uuid = UUID.fromString(id);
        return service.addSubtask(uuid, subtask);
    }
}
