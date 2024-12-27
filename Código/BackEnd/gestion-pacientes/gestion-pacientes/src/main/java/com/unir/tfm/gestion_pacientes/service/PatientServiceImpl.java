package com.unir.tfm.gestion_pacientes.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unir.tfm.gestion_pacientes.client.QuestionnaireClient;
import com.unir.tfm.gestion_pacientes.model.QuestionnaireDto;
import com.unir.tfm.gestion_pacientes.model.ResponseDto;

@Service
public class PatientServiceImpl implements PatientService {
    private static final Logger log = LoggerFactory.getLogger(QuestionnaireClient.class);

    @Autowired
    private QuestionnaireClient questionnaireClient;

    @Override
    public List<QuestionnaireDto> getPendingQuestionnaires(Long patientId) {
        return questionnaireClient.getPendingQuestionnaires(patientId);
    }

    @Override
    public void submitQuestionnaire(Long questionnaireId, Long patientId, Map<String, List<ResponseDto>> responses) {
        try {
            log.info("Submitting questionnaire responses for patientId: {}, questionnaireId: {}", patientId,
                    questionnaireId);
            questionnaireClient.submitAnswers(questionnaireId, patientId, responses);
        } catch (RuntimeException e) {
            log.error("Error al procesar las respuestas del cuestionario: {}", e.getMessage());
            throw e; // Re-lanzar la excepción para que el controlador la capture
        }
    }

    @Override
    public Double calculatePatientProgress(Long patientId, Long questionnaireId) {
        log.info("Calculating progress for patientId: {}, questionnaireId: {}", patientId, questionnaireId);
        return questionnaireClient.getPatientProgress(patientId, questionnaireId);
    }

    @Override
    public List<QuestionnaireDto> getCompletedQuestionnaires(Long patientId) {
        log.info("Fetching completed questionnaires for patientId: {}", patientId);
        return questionnaireClient.getCompletedQuestionnaires(patientId);
    }

    @Override
    public Map<String, List<QuestionnaireDto>> getQuestionnaireResponses(Long patientId, Long questionnaireId) {
        log.info("Fetching questionnaire responses for patientId: {}, questionnaireId: {}", patientId, questionnaireId);
        return questionnaireClient.getQuestionnaireResponses(patientId, questionnaireId);
    }

}
