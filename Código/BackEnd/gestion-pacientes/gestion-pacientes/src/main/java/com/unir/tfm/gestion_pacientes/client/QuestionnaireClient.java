package com.unir.tfm.gestion_pacientes.client;

import com.unir.tfm.gestion_pacientes.model.QuestionnaireDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class QuestionnaireClient {

    @Autowired
    private RestTemplate restTemplate;

    public List<QuestionnaireDto> getPendingQuestionnaires(Long patientId) {
        String url = "http://localhost:8762/ms-gestion-cuestionarios/questionnaires/available/" + patientId;
        QuestionnaireDto[] response = restTemplate.getForObject(url, QuestionnaireDto[].class);
        return Arrays.asList(response);
    }

    public void submitAnswers(Long patientQuestionnaireId, String responses) {
        String url = "http://ms-gestion-cuestionarios/questionnaires/submit";
        restTemplate.postForObject(url,
                new SubmitAnswerRequest(patientQuestionnaireId, responses),
                Void.class);
    }

    // Clase interna para la solicitud
    private static class SubmitAnswerRequest {
        private Long patientQuestionnaireId;
        private String response;

        public SubmitAnswerRequest(Long patientQuestionnaireId, String response) {
            this.patientQuestionnaireId = patientQuestionnaireId;
            this.response = response;
        }

        @SuppressWarnings("unused") // Podría ser útil en el futuro
        public Long getPatientQuestionnaireId() {
            return patientQuestionnaireId;
        }

        @SuppressWarnings("unused") // Podría ser útil en el futuro
        public String getResponse() {
            return response;
        }
    }
}
