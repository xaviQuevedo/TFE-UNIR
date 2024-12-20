package com.unir.tfm.gestion_pacientes.controller;

import com.unir.tfm.gestion_pacientes.service.PatientService;
import com.unir.tfm.gestion_pacientes.model.QuestionnaireDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/{patientId}/pending-questionnaires")
    public ResponseEntity<List<QuestionnaireDto>> getPendingQuestionnaires(@PathVariable Long patientId) {
        List<QuestionnaireDto> questionnaires = patientService.getPendingQuestionnaires(patientId);
        return ResponseEntity.ok(questionnaires);
    }

    @PostMapping("/{patientQuestionnaireId}/submit-responses")
    public ResponseEntity<String> submitResponses(
            @PathVariable Long patientQuestionnaireId,
            @RequestBody String responses) {
        try {
            patientService.submitQuestionnaire(patientQuestionnaireId, responses);
            return ResponseEntity.ok("Respuestas enviadas correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
