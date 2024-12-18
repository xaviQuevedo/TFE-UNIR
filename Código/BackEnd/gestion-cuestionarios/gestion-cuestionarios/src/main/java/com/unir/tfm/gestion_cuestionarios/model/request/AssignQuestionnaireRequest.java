package com.unir.tfm.gestion_cuestionarios.model.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignQuestionnaireRequest {
    private Long patientId; // ID del paciente
    private List<Long> questionnaireIds; // ID del cuestionario
    private Long physiotherapistId; // ID del fisioterapeuta

}
