package com.example.mytask.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDto {

    @NotBlank(message = "일정 내용은 필수입니다.")
    @Size(min = 3, max = 200, message = "일정 내용은 3자 이상 200자 이하이어야 합니다.")
    private String taskName;

    @NotNull(message = "담당자 정보는 필수입니다.")
    private Long assigneeId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}