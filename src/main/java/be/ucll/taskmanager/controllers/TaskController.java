package be.ucll.taskmanager.controllers;

import be.ucll.taskmanager.domain.DTO.SubtaskDTO;
import be.ucll.taskmanager.domain.DTO.TaskDTO;
import be.ucll.taskmanager.domain.service.Subtask;
import be.ucll.taskmanager.domain.service.TaskServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskServiceImp service;

    @GetMapping
    public String tasksPage(Model model){
        model.addAttribute("tasks", service.getAllTasks());
        return "tasks";
    }
    @GetMapping("/{id}")
    public String getTaskDTO(Model model, @PathVariable("id") String id){
        TaskDTO taskDTO = service.getTaskDTO(UUID.fromString(id));
        model.addAttribute("task",taskDTO);
        return "taskDetail";
    }
    @GetMapping("/new")
    public String addTaskPage(Model model){
        model.addAttribute("taskDTO", new TaskDTO());
        return "addTask";
    }
    @PostMapping("/addTask")
    public String addTask(Model model, @ModelAttribute @Valid TaskDTO taskDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println(bindingResult);
            return "addTask";
        }
        service.addTask(taskDTO);
        return "redirect:/tasks";
    }
    //What happens when you click on "EDIT TASK"
    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") String id){
        TaskDTO taskDTO = service.getTaskDTO(UUID.fromString(id));
        model.addAttribute("taskDTO", taskDTO);
        return "editTask";
    }
    //Method to be executed when you submit the edited task
    @PostMapping("/editTask")
    public String editTask(Model model, @ModelAttribute @Valid TaskDTO taskDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "editTask";
        }
        service.editTask(taskDTO);
        return "redirect:/tasks/" + taskDTO.getUuid();
    }
    @GetMapping("/{id}/sub/create")
    public String pageCreateSubtask(Model model, @PathVariable("id") String id){
        Subtask subtask = new Subtask();
        TaskDTO mainTask = service.getTaskDTO(UUID.fromString(id));
        model.addAttribute("subtask", subtask);
        model.addAttribute("task",mainTask);
        return "addSubtask";
    }
    @PostMapping("/addSubtask")
    public String addSubtask(Model model, @RequestParam(value="idMainTask") String id, @ModelAttribute @Valid SubtaskDTO subtaskDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            model.addAttribute("subtaskDTO", subtaskDTO);
            TaskDTO mainTask = service.getTaskDTO(UUID.fromString(id));
            model.addAttribute("task", mainTask);
            return "addSubtask";
        }
        UUID uuid = UUID.fromString(id);
        service.addSubtask(uuid, subtaskDTO);
        return "redirect:/tasks/" + id;
    }
}
