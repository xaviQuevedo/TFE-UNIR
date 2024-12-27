package com.unir.tfm.gestion_cuestionarios.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.unir.tfm.gestion_cuestionarios.model.request.AssignQuestionnaireRequest;
import com.unir.tfm.gestion_cuestionarios.model.response.CustomResponseDto;
import com.unir.tfm.gestion_cuestionarios.model.response.QuestionnaireResponse;
import com.unir.tfm.gestion_cuestionarios.model.response.QuestionnaireResponseDto;
import com.unir.tfm.gestion_cuestionarios.model.response.ResponseDto;

public interface QuestionnaireService {
    void assignQuestionnaire(AssignQuestionnaireRequest request);

    QuestionnaireResponseDto getQuestionnaire(Long questionnaireId);

    List<QuestionnaireResponseDto> getAvailableQuestionnaires(Long patientId);

    List<QuestionnaireResponseDto> getNotAssignedQuestionnaires(Long patientId);

    QuestionnaireResponseDto getQuestionnaireWithQuestions(Long questionnaireId);

    void submitAnswerAndUpdateStatus(Long questionnaireId, Long patientId, Map<String, List<ResponseDto>> response);

    double calculateProgress(Long patientId, long questionnaireId);

    Map<String, List<QuestionnaireResponse>> getQuestionnaireResponsesGroupedByQuestion(Long questionnaireId,
            Long patientId);

    List<QuestionnaireResponseDto> getCompletedQuestionnaires(Long patientId);

    Map<Date, List<CustomResponseDto>> getQuestionnaireResponsesGroupedByDate(Long questionnaireId, Long patientId);

    Map<Date, Integer> getScoreByDate(Long questionnaireId, Long patientId);

    List<Map<String, Object>> getScoresBySession(Long questionnaireId, Long patientId);

}
