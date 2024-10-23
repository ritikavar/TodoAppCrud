package com.example.todoApp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TodoDTO {

  private Long id;

  @NotBlank
  private String title;

  private String description;
}
