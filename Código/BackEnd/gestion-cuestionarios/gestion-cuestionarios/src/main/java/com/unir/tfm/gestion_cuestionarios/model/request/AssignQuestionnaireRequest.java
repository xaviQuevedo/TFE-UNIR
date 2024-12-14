package com.unir.tfm.gestion_cuestionarios.model.request;

import lombok.Data;

@Data
public class AssignQuestionnaireRequest {
    private Long userId;
    private Long questionnaireId;

}
