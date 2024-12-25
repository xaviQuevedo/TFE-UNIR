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

            // Llamar al cliente con los datos correctos
            questionnaireClient.submitAnswers(questionnaireId, patientId, responses);

        } catch (Exception e) {
            log.error("Error al enviar respuestas al cliente: {}", e.getMessage());

            throw new RuntimeException("Error al procesar las respuestas del cuestionario", e);
        }
    }
}
