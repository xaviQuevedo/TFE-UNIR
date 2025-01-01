package com.unir.tfm.gestion_cuestionarios.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalHistoryDto {
    private Long patientId;
    private List<QuestionnaireHistory> history;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionnaireHistory {
        private String questionnaireTitle;
        private Date assignedAt;
        private Date completedAt;
        private Integer score;
        private String comments;
        private Double averageScore; // Evolución promedio
    }
}