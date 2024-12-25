package com.unir.tfm.gestion_cuestionarios.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unir.tfm.gestion_cuestionarios.model.request.AssignQuestionnaireRequest;
import com.unir.tfm.gestion_cuestionarios.model.response.QuestionnaireResponseDto;
import com.unir.tfm.gestion_cuestionarios.model.response.ResponseDto;
import com.unir.tfm.gestion_cuestionarios.service.QuestionnaireService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/questionnaires")
@RequiredArgsConstructor
public class QuestionnaireController {

    @Autowired
    private QuestionnaireService questionnaireService;

    @PostMapping("/assign")
    public ResponseEntity<String> assignQuestionnaire(@RequestBody AssignQuestionnaireRequest request) {
        try {
            questionnaireService.assignQuestionnaire(request);
            return ResponseEntity.ok("Cuestionarios asignado al paciente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{questionnaireId}/submit")
    public ResponseEntity<String> submitQuestionnaireResponses(
            @PathVariable Long questionnaireId,
            @RequestParam Long patientId,
            @RequestBody Map<String, List<ResponseDto>> responses) {
        try {
            questionnaireService.submitAnswerAndUpdateStatus(questionnaireId, patientId, responses);
            return ResponseEntity.ok("Respuestas guardadas y cuestionario completado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /*
     * @PostMapping("/submit")
     * public ResponseEntity<String> submitAnswer(@RequestBody SubmitAnswerRequest
     * request) {
     * try {
     * questionnaireService.submitAnswer(request);
     * return ResponseEntity.ok("Respuesta guardada correctamente.");
     * } catch (RuntimeException e) {
     * return ResponseEntity.badRequest().body(e.getMessage());
     * }
     * }
     */

    @GetMapping("/{questionnaireId}")
    public ResponseEntity<QuestionnaireResponseDto> getQuestionnaire(@PathVariable Long questionnaireId) {
        return ResponseEntity.ok(questionnaireService.getQuestionnaire(questionnaireId));
    }

    @GetMapping("/available/{patientId}")
    public ResponseEntity<List<QuestionnaireResponseDto>> getAvailableQuestionnaires(@PathVariable Long patientId) {
        List<QuestionnaireResponseDto> availableQuestionnaires = questionnaireService
                .getAvailableQuestionnaires(patientId);
        return ResponseEntity.ok(availableQuestionnaires);
    }

    @GetMapping("/{questionnaireId}/questions")
    public ResponseEntity<QuestionnaireResponseDto> getQuestionnaireWithQuestions(@PathVariable Long questionnaireId) {
        try {
            QuestionnaireResponseDto questionnaire = questionnaireService
                    .getQuestionnaireWithQuestions(questionnaireId);
            return ResponseEntity.ok(questionnaire);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
