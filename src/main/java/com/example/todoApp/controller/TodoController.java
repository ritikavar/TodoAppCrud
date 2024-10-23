package com.example.todoApp.controller;

import com.example.todoApp.dto.TodoDTO;
import com.example.todoApp.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

  @Autowired
  private TodoService todoService;

  @PostMapping
  public ResponseEntity<TodoDTO> createTodo(@Valid @RequestBody TodoDTO todoDTO, Principal principal) {
    TodoDTO createdTodo = todoService.createTodo(todoDTO, principal.getName());
    return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<Page<TodoDTO>> getTodos(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int limit,
          Principal principal) {
    Page<TodoDTO> todos = todoService.getTodos(page, limit, principal.getName());
    return ResponseEntity.ok(todos);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TodoDTO> updateTodo(@PathVariable Long id, @Valid @RequestBody TodoDTO todoDTO, Principal principal) {
    TodoDTO updatedTodo = todoService.updateTodo(id, todoDTO, principal.getName());
    return ResponseEntity.ok(updatedTodo);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TodoDTO> getTodoById(@PathVariable Long id, Principal principal) {
    TodoDTO todoDTO = todoService.getTodoById(id, principal.getName());
    return ResponseEntity.ok(todoDTO);
  }


  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTodo(@PathVariable Long id, Principal principal) {
    todoService.deleteTodo(id, principal.getName());
    return ResponseEntity.noContent().build();
  }
}

