package be.ucll.taskmanager.controllers;

import be.ucll.taskmanager.service.Subtask;
import be.ucll.taskmanager.service.Task;
import be.ucll.taskmanager.service.TaskServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Controller
public class TaskController {
    //DTO GEBRUIKEN IN CONTROLLER
    @Autowired
    private TaskServiceImp service;

    @GetMapping("/tasks")
    public String tasksPage(Model model){
        model.addAttribute("tasks", service.getAllTasks());
        return "tasks";
    }
    @GetMapping("/")
    public String indexPagina(Model model){
        return "redirect:/tasks";
    }
    @GetMapping("/tasks/{id}")
    public String getTask(Model model, @PathVariable("id") String id){
        Task task = service.getTask(UUID.fromString(id));
        model.addAttribute("task",task);
        return "taskDetail";
    }
    @GetMapping("/tasks/new")
    public String addTaskPage(Model model){
        model.addAttribute("task", new Task());
        return "addTask";
    }
    @PostMapping("/addTask")
    public String addTask(Model model, @ModelAttribute @Valid Task task, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "addTask";
        }
        service.addTask(task);
        return "redirect:/tasks";
    }
    //What happens when you click on "EDIT TASK"
    @GetMapping("/tasks/edit/{id}")
    public String edit(Model model, @PathVariable("id") String id){
        Task task = service.getTask(UUID.fromString(id));
        model.addAttribute("task", task);
        return "editTask";
    }
    //Method to be executed when you submit the edited task
    @PostMapping("/editTask")
    public String editTask(Model model, @ModelAttribute @Valid Task task, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "editTask";
        }
        service.editTask(task.getUuid(),task.getTitle(), task.getDescription(), task.getdate());
        return "redirect:/tasks/" + task.getUuid();
    }
    @GetMapping("/tasks/{id}/sub/create")
    public String pageCreateSubtask(Model model, @PathVariable("id") String id){
        Subtask subtask = new Subtask();
        Task mainTask = service.getTask(UUID.fromString(id));
        model.addAttribute("subtask", subtask);
        model.addAttribute("task",mainTask);
        return "addSubtask";
    }
    @PostMapping("/addSubtask")
    public String addSubtask(Model model, @RequestParam(value="id") String id, @ModelAttribute @Valid Subtask subtask, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            model.addAttribute("subtask", subtask);
            Task mainTask = service.getTask(UUID.fromString(id));
            model.addAttribute("task", mainTask);
            return "addSubtask";
        }
        UUID uuid = UUID.fromString(id);
        service.addSubtask(uuid, subtask);
        return "redirect:/tasks/" + id;
    }
}
