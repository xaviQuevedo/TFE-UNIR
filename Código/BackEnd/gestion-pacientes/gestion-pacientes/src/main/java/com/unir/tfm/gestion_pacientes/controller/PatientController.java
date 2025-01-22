package com.unir.tfm.gestion_pacientes.controller;

import com.unir.tfm.gestion_pacientes.service.PatientService;
import com.unir.tfm.gestion_pacientes.client.QuestionnaireClient;
import com.unir.tfm.gestion_pacientes.model.QuestionnaireDto;
import com.unir.tfm.gestion_pacientes.model.ResponseDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private static final Logger log = LoggerFactory.getLogger(QuestionnaireClient.class);

    @Autowired
    private PatientService patientService;

    // @GetMapping("/{patientId}/pending-questionnaires")
    // Obtener los cuestionarios pendientes de un paciente
    @GetMapping("/{id}/questionnaires/pending")
    public ResponseEntity<List<QuestionnaireDto>> getPendingQuestionnaires(@PathVariable("id") Long patientId) {
        List<QuestionnaireDto> questionnaires = patientService.getPendingQuestionnaires(patientId);
        return ResponseEntity.ok(questionnaires);
    }

    // @PostMapping("/{questionnaireId}/responses")
    // Enviar respuestas a un cuestionario
    @PostMapping("/{id}/questionnaires/{questionnaireId}/responses")
    public ResponseEntity<String> submitResponses(
            @PathVariable("id") Long patientId,
            @PathVariable Long questionnaireId,
            @RequestBody Map<String, List<ResponseDto>> requestBody) {

        try {
            patientService.submitQuestionnaire(questionnaireId, patientId, requestBody);
            return ResponseEntity.ok("Respuestas enviadas correctamente.");
        } catch (RuntimeException e) {
            log.error("Error en submitResponses: {}", e.getMessage());

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // @GetMapping("/{patientId}/progress/{questionnaireId}")
    // Obtener el progreso de un cuestionario para un paciente
    @GetMapping("/{id}/questionnaires/{questionnaireId}/progress")
    public ResponseEntity<Double> getPatientProgress(
            @PathVariable("id") Long patientId,
            @PathVariable Long questionnaireId) {
        try {
            Double progress = patientService.calculatePatientProgress(patientId, questionnaireId);
            return ResponseEntity.ok(progress);

        } catch (RuntimeException e) {
            log.error("Error en getPatientProgress: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    // @GetMapping("/{patientId}/completed-questionnaires")
    // Obtener los cuestionarios completados de un paciente
    @GetMapping("/{id}/questionnaires/completed")
    public ResponseEntity<List<QuestionnaireDto>> getCompletedQuestionnaires(@PathVariable("id") Long patientId) {
        try {
            List<QuestionnaireDto> completedQuestionnaires = patientService.getCompletedQuestionnaires(patientId);
            return ResponseEntity.ok(completedQuestionnaires);
        } catch (RuntimeException e) {
            log.error("Error al obtener cuestionarios completados para patientId {}: {}", patientId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // @GetMapping("/{patientId}/questionnaires/{questionnaireId}/responses")
    // Obtener respuestas de un cuestionario específico de un paciente
    @GetMapping("/{id}/questionnaires/{questionnaireId}/responses")
    public ResponseEntity<Map<String, List<QuestionnaireDto>>> getQuestionnaireResponses(
            @PathVariable("id") Long patientId,
            @PathVariable Long questionnaireId) {
        try {
            Map<String, List<QuestionnaireDto>> responses = patientService.getQuestionnaireResponses(patientId,
                    questionnaireId);
            return ResponseEntity.ok(responses);
        } catch (RuntimeException e) {
            log.error("Error fetching questionnaire responses: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
