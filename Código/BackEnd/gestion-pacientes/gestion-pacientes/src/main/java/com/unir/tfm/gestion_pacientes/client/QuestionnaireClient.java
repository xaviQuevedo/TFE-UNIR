package com.unir.tfm.gestion_pacientes.client;

import com.unir.tfm.gestion_pacientes.model.QuestionnaireDto;
import com.unir.tfm.gestion_pacientes.model.ResponseDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class QuestionnaireClient {

        private static final Logger log = LoggerFactory.getLogger(QuestionnaireClient.class);


    @Autowired
    private RestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8762/ms-gestion-cuestionarios/questionnaires";

    public List<QuestionnaireDto> getPendingQuestionnaires(Long patientId) {
        String url = BASE_URL + "/available/" + patientId;
        QuestionnaireDto[] response = restTemplate.getForObject(url, QuestionnaireDto[].class);
        return Arrays.asList(response);
    }

    public void submitAnswers(Long questionnaireId, Long patientId, Map<String, List<ResponseDto>> responses) {

        String url = BASE_URL + "/" + questionnaireId + "/submit?patientId=" + patientId;
        log.info("Submitting answers to URL: {}", url);
        log.info("Payload: {}", responses);
        restTemplate.postForObject(url, responses,Void.class);
    }

    // Clase interna para la solicitud
    /* private static class SubmitAnswerRequest {
        private Long patientQuestionnaireId;
        private String response;

        public SubmitAnswerRequest(Long patientQuestionnaireId, String response) {
            this.patientQuestionnaireId = patientQuestionnaireId;
            this.response = response;
        } */

        /* SuppressWarnings("unused") // Podría ser útil en el futuro
        public Long getPatientQuestionnaireId() {
            return patientQuestionnaireId;
        }

        @SuppressWarnings("unused") // Podría ser útil en el futuro
        public String getResponse() {
            return response;
        }
    } */
}
