package com.unir.tfm.gestion_cuestionarios.model.request;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SubmitAnswerRequest {
    private Long patientQuestionnaireId;
    private String response;
    private LocalDateTime completedAt;


}
