package com.unir.tfm.gestion_cuestionarios.service;

import java.util.List;
import java.util.Map;

import com.unir.tfm.gestion_cuestionarios.model.request.AssignQuestionnaireRequest;
import com.unir.tfm.gestion_cuestionarios.model.response.QuestionnaireResponseDto;
import com.unir.tfm.gestion_cuestionarios.model.response.ResponseDto;

public interface QuestionnaireService {
    void assignQuestionnaire(AssignQuestionnaireRequest request);

    QuestionnaireResponseDto getQuestionnaire(Long questionnaireId);

    List<QuestionnaireResponseDto> getAvailableQuestionnaires(Long patientId);

    QuestionnaireResponseDto getQuestionnaireWithQuestions(Long questionnaireId);

    void submitAnswerAndUpdateStatus(Long questionnaireId, Long patientId, Map<String, List<ResponseDto>> response);
}
