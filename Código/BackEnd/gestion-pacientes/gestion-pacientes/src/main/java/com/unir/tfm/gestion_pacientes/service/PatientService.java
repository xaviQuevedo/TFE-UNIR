package com.unir.tfm.gestion_pacientes.service;

import java.util.List;
import java.util.Map;

import com.unir.tfm.gestion_pacientes.model.QuestionnaireDto;
import com.unir.tfm.gestion_pacientes.model.ResponseDto;

public interface PatientService {
    List<QuestionnaireDto> getPendingQuestionnaires(Long patientId);

    void submitQuestionnaire(Long questionnaireId, Long patientId, Map<String, List<ResponseDto>> responses);

    Double calculatePatientProgress(Long patientId, Long questionnaireId);

    List<QuestionnaireDto> getCompletedQuestionnaires(Long patientId);
    Map<String, List<QuestionnaireDto>> getQuestionnaireResponses(Long patientId, Long questionnaireId);
}
