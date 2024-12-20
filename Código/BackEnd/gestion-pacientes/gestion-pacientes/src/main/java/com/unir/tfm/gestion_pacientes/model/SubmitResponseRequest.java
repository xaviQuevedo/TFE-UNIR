package com.unir.tfm.gestion_pacientes.model;

import lombok.Data;
import java.util.Map;

@Data
public class SubmitResponseRequest {
    private Long questionnaireId;
    private Map<Long, String> responses; // ID de la pregunta -> Respuesta del paciente
}
