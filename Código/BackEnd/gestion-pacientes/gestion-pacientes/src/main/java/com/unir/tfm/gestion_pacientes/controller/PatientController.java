package com.unir.tfm.gestion_pacientes.controller;

import com.unir.tfm.gestion_pacientes.service.PatientService;
import com.unir.tfm.gestion_pacientes.client.QuestionnaireClient;
import com.unir.tfm.gestion_pacientes.model.QuestionnaireDto;
import com.unir.tfm.gestion_pacientes.model.ResponseDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/{patientId}/pending-questionnaires")
    public ResponseEntity<List<QuestionnaireDto>> getPendingQuestionnaires(@PathVariable Long patientId) {
        List<QuestionnaireDto> questionnaires = patientService.getPendingQuestionnaires(patientId);
        return ResponseEntity.ok(questionnaires);
    }

    @PostMapping("/{questionnaireId}/submit-responses")
    public ResponseEntity<String> submitResponses(
            @RequestParam Long patientId,
            @PathVariable Long questionnaireId,
            @RequestBody Map<String, List<ResponseDto>> requestBody) { // Captura el campo "responses"
        System.out.println("Entre al metodo submitResponses");

        try {
            patientService.submitQuestionnaire(questionnaireId, patientId, requestBody);
            return ResponseEntity.ok("Respuestas enviadas correctamente.");
        } catch (RuntimeException e) {
            log.error("Error en submitResponses: {}", e.getMessage());

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
