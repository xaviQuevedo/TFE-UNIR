package com.unir.tfm.gestion_pacientes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unir.tfm.gestion_pacientes.client.QuestionnaireClient;
import com.unir.tfm.gestion_pacientes.model.QuestionnaireDto;

@Service
public class PatientServiceImpl implements PatientService {
    
    @Autowired
    private QuestionnaireClient questionnaireClient;

    @Override
    public List<QuestionnaireDto> getPendingQuestionnaires(Long patientId) {
        return questionnaireClient.getPendingQuestionnaires(patientId);
    }

    @Override
    public void submitQuestionnaire(Long patientQuestionnaireId, String responses) {
        questionnaireClient.submitAnswers(patientQuestionnaireId, responses);
    }

}
