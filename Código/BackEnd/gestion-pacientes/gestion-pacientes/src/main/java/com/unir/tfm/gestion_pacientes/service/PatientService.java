package com.unir.tfm.gestion_pacientes.service;

import java.util.List;

import com.unir.tfm.gestion_pacientes.model.QuestionnaireDto;

public interface PatientService {
    List<QuestionnaireDto> getPendingQuestionnaires(Long patientId);

    void submitQuestionnaire(Long patientId, String responses);
}
