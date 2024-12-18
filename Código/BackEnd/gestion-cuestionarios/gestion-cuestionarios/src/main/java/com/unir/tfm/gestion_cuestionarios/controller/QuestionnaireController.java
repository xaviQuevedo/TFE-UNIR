package com.unir.tfm.gestion_cuestionarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unir.tfm.gestion_cuestionarios.model.request.AssignQuestionnaireRequest;
import com.unir.tfm.gestion_cuestionarios.model.request.SubmitAnswerRequest;
import com.unir.tfm.gestion_cuestionarios.model.response.QuestionnaireResponseDto;
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

    @PostMapping("/submit")
    public ResponseEntity<String> submitAnswer(@RequestBody SubmitAnswerRequest request) {
        questionnaireService.submitAnswer(request);
        return ResponseEntity.ok("Answer submitted");
    }

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

}
