package com.unir.tfm.gestion_cuestionarios.service;

import java.util.List;

import com.unir.tfm.gestion_cuestionarios.model.request.AssignQuestionnaireRequest;
import com.unir.tfm.gestion_cuestionarios.model.request.SubmitAnswerRequest;
import com.unir.tfm.gestion_cuestionarios.model.response.QuestionnaireResponseDto;

public interface QuestionnaireService {
    void assignQuestionnaire(AssignQuestionnaireRequest request);

    void submitAnswer(SubmitAnswerRequest request);

    QuestionnaireResponseDto getQuestionnaire(Long questionnaireId);

    List<QuestionnaireResponseDto> getAllQuestionnaires();

}
