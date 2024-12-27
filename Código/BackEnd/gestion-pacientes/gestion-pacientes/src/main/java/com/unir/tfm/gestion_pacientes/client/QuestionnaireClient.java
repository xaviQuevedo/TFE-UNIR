package com.unir.tfm.gestion_pacientes.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unir.tfm.gestion_pacientes.model.QuestionnaireDto;
import com.unir.tfm.gestion_pacientes.model.ResponseDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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
        System.out.println("Response: " + response);
        return Arrays.asList(response);
    }

    public void submitAnswers(Long questionnaireId, Long patientId, Map<String, List<ResponseDto>> responses) {
        String url = BASE_URL + "/" + questionnaireId + "/submit?patientId=" + patientId;
        log.info("Submitting answers to URL: {}", url);
        log.info("Payload: {}", responses);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, responses, String.class);
            log.info("Response from questionnaire service: {}", response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP error when submitting answers: {}", e.getResponseBodyAsString());
            throw new RuntimeException("Error in questionnaire service response: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error when submitting answers: {}", e.getMessage());
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        }
    }

    public Double getPatientProgress(Long patientId, Long questionnaireId) {
        String url = BASE_URL + "/" + patientId + "/progress/" + questionnaireId;
        log.info("Fetching progress from URL: {}", url);
        try {
            ResponseEntity<Double> response = restTemplate.getForEntity(url, Double.class);
            log.info("Progress received: {}", response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP error when fetching progress: {}", e.getResponseBodyAsString());
            throw new RuntimeException("Error in questionnaire service response: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error when fetching progress: {}", e.getMessage());
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        }
    }

    public List<QuestionnaireDto> getCompletedQuestionnaires(Long patientId) {
        String url = BASE_URL + "/" + patientId + "/completed-questionnaires";
        log.info("Fetching completed questionnaires from URL: {}", url);
        try {
            ResponseEntity<QuestionnaireDto[]> response = restTemplate.getForEntity(url, QuestionnaireDto[].class);
            return Arrays.asList(response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP error when fetching completed questionnaires: {}", e.getResponseBodyAsString());
            throw new RuntimeException("Error in questionnaire service response: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error when fetching completed questionnaires: {}", e.getMessage());
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        }
    }

    public Map<String, List<QuestionnaireDto>> getQuestionnaireResponses(Long patientId, Long questionnaireId) {
        String url = BASE_URL + "/" + patientId + "/questionnaires/" + questionnaireId + "/responses";
        log.info("Fetching responses for patientId: {}, questionnaireId: {}", patientId, questionnaireId);
        try {
            @SuppressWarnings("rawtypes")
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            ObjectMapper mapper = new ObjectMapper();
            // Convertir el resultado a Map<String, List<QuestionnaireDto>>
            Map<String, List<QuestionnaireDto>> convertedResponse = mapper.convertValue(
                response.getBody(),
                new TypeReference<Map<String, List<QuestionnaireDto>>>() {}
            );
            return convertedResponse;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP error when fetching responses: {}", e.getResponseBodyAsString());
            throw new RuntimeException("Error in questionnaire service response: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error when fetching responses: {}", e.getMessage());
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        }
    }

}
