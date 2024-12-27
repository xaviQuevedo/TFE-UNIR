package com.unir.tfm.gestion_cuestionarios.model.response;

import java.util.Date;

public class CustomResponseDto {

    private Date createdAt;
    private String questionText;
    private String answer;

    public CustomResponseDto(Date createdAt, String questionText, String answer) {
        this.createdAt = createdAt;
        this.questionText = questionText;
        this.answer = answer;
    }

    // Getters y setters
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
