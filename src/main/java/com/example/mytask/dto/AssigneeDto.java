package com.example.mytask.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AssigneeDto {

    @NotBlank(message = "이름을 입력해 주세요!")
    @Size(max = 100, message = "이름은 100자를 초과할 수 없습니다.")
    private String name;

    @NotBlank(message = "이메일 입력은 필수입니다.")
    @Email(message = "유효한 이메일을 입력해주세요.")
    private String email;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
