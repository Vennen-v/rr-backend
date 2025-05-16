package com.therogueroad.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostTitleAndContentRequest {
    @NotBlank
    private String content;
    @NotBlank
    private String title;
    @NotBlank
    private String postImg;

    public @NotBlank String getContent() {
        return content;
    }

    public void setContent(@NotBlank String content) {
        this.content = content;
    }

    public @NotBlank String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank String title) {
        this.title = title;
    }
}
