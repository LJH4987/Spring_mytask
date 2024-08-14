package com.example.mytask.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TaskDto {

    @NotBlank(message = "일정 제목은 필수입니다.")
    @Size(max = 200, message = "일정 제목은 200자를 초과할 수 없습니다.")
    private String taskName;

    @NotNull(message = "담당자 이름은 필수입니다.")
    private Long assigneeId;

    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    private String password;

    // Getters and Setters
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
