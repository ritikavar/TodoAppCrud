package com.example.todoApp.service;

import com.example.todoApp.dto.TodoDTO;
import com.example.todoApp.exception.CustomException;
import com.example.todoApp.model.Todo;
import com.example.todoApp.model.User;
import com.example.todoApp.repository.TodoRepository;
import com.example.todoApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

@Service
public class TodoService {

  @Autowired
  private TodoRepository todoRepository;

  @Autowired
  private UserRepository userRepository;

  public TodoDTO createTodo(TodoDTO todoDTO, String userEmail) {
    User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new CustomException("User not found"));

    Todo todo = new Todo();
    todo.setTitle(todoDTO.getTitle());
    todo.setDescription(todoDTO.getDescription());
    todo.setUser(user);
    todoRepository.save(todo);

    return mapToDTO(todo);
  }

  public Page<TodoDTO> getTodos(int page, int limit, String userEmail) {
    User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new CustomException("User not found"));

    Pageable pageable = (Pageable) PageRequest.of(page, limit, Sort.by("id").descending());
    Page<Todo> todos = todoRepository.findByUserId(user.getId(), pageable);

    return todos.map(this::mapToDTO);
  }

  public TodoDTO updateTodo(Long id, TodoDTO todoDTO, String userEmail) {
    User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new CustomException("User not found"));

    Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new CustomException("Todo not found"));

    if (!todo.getUser().getId().equals(user.getId())) {
      throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
    }

    todo.setTitle(todoDTO.getTitle());
    todo.setDescription(todoDTO.getDescription());
    todoRepository.save(todo);

    return mapToDTO(todo);
  }

  public void deleteTodo(Long id, String userEmail) {
    User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new CustomException("User not found"));

    Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new CustomException("Todo not found"));

    if (!todo.getUser().getId().equals(user.getId())) {
      throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
    }

    todoRepository.delete(todo);
  }

  private TodoDTO mapToDTO(Todo todo) {
    TodoDTO dto = new TodoDTO();
    dto.setId(todo.getId());
    dto.setTitle(todo.getTitle());
    dto.setDescription(todo.getDescription());
    return dto;
  }

  // Retrieve Todo by id ensuring it belongs to the given user
  public TodoDTO getTodoById(Long id, String userEmail) {
    User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

    Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new CustomException("Todo not found or access denied", HttpStatus.FORBIDDEN));

    // Convert Todo to TodoDTO (implement this conversion as needed)
    return convertToDTO(todo);
  }

  private TodoDTO convertToDTO(Todo todo) {
    // Implement your DTO conversion logic here
    TodoDTO dto = new TodoDTO();
    dto.setId(todo.getId());
    dto.setTitle(todo.getTitle());
    dto.setDescription(todo.getDescription());
    // Add other fields as necessary
    return dto;
  }


}