package com.unir.tfm.gestion_cuestionarios.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.unir.tfm.gestion_cuestionarios.model.response.CustomResponseDto;
import com.unir.tfm.gestion_cuestionarios.model.response.QuestionnaireResponseDto;
import com.unir.tfm.gestion_cuestionarios.model.response.ResponseDto;
import com.unir.tfm.gestion_cuestionarios.service.Questionnarie.QuestionnaireService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/questionnaires")
@RequiredArgsConstructor
public class QuestionnaireController {

    @Autowired
    private QuestionnaireService questionnaireService;

    private static final Logger log = LoggerFactory.getLogger(QuestionnaireController.class);

    // @PostMapping("/assign")
    // Asignar cuestionarios a pacientes
    @PostMapping("/assignments")
    public ResponseEntity<String> assignQuestionnaire(@RequestBody AssignQuestionnaireRequest request) {
        try {
            questionnaireService.assignQuestionnaire(request);
            return ResponseEntity.ok("Cuestionarios asignado al paciente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // @PostMapping("/{questionnaireId}/submit")
    // Guardar respuestas de un cuestionario
    @PostMapping("/{id}/responses")
    public ResponseEntity<String> submitQuestionnaireResponses(
            @PathVariable("id") Long questionnaireId,
            @RequestParam Long patientId,
            @RequestBody Map<String, List<ResponseDto>> responses) {
        try {
            questionnaireService.submitAnswerAndUpdateStatus(questionnaireId, patientId, responses);
            return ResponseEntity.ok("Respuestas guardadas y cuestionario completado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // @GetMapping("/{questionnaireId}")
    // Obtener un cuestionario por ID
    @GetMapping("/{id}")
    public ResponseEntity<QuestionnaireResponseDto> getQuestionnaire(@PathVariable("id") Long questionnaireId) {
        return ResponseEntity.ok(questionnaireService.getQuestionnaire(questionnaireId));
    }

    // @GetMapping("/available/{patientId}")
    // Obtener cuestionarios disponibles para un paciente
    @GetMapping("/patients/{id}/available")
    public ResponseEntity<List<QuestionnaireResponseDto>> getAvailableQuestionnaires(
            @PathVariable("id") Long patientId) {
        List<QuestionnaireResponseDto> availableQuestionnaires = questionnaireService
                .getAvailableQuestionnaires(patientId);
        return ResponseEntity.ok(availableQuestionnaires);
    }

    // @GetMapping("/not-assigned/{patientId}")
    // Obtener cuestionarios no asignados a un paciente
    @GetMapping("/patients/{id}/unassigned")
    public ResponseEntity<List<QuestionnaireResponseDto>> getNotAssignedQuestionnaires(
            @PathVariable("id") Long patientId) {
        try {
            List<QuestionnaireResponseDto> notAssignedQuestionnaires = questionnaireService
                    .getNotAssignedQuestionnaires(patientId);
            return ResponseEntity.ok(notAssignedQuestionnaires);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // @GetMapping("/{questionnaireId}/questions")
    // Obtener las preguntas de un cuestionario
    @GetMapping("/{id}/questions")
    public ResponseEntity<QuestionnaireResponseDto> getQuestionnaireWithQuestions(
            @PathVariable("id") Long questionnaireId) {
        try {
            QuestionnaireResponseDto questionnaire = questionnaireService
                    .getQuestionnaireWithQuestions(questionnaireId);
            return ResponseEntity.ok(questionnaire);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // @GetMapping("/{patientId}/progress/{questionnaireId}")
    // Obtener progreso de un paciente en un cuestionario
    @GetMapping("/patients/{id}/progress/{questionnaireId}")

    public ResponseEntity<Double> getPatientProgress(
            @PathVariable("id") Long patientId,
            @PathVariable Long questionnaireId) {
        try {
            double progress = questionnaireService.calculateProgress(patientId, questionnaireId);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // @GetMapping("/{patientId}/questionnaires/{questionnaireId}/responses")
    // Obtener respuestas de un cuestionario por paciente
    @GetMapping("/patients/{id}/questionnaires/{questionnaireId}/responses")
    public ResponseEntity<?> getQuestionnaireResponses(
            @PathVariable("id") Long patientId,
            @PathVariable Long questionnaireId) {
        try {
            // Lista de cuestionarios que miden por score
            List<Long> scoreBasedQuestionnaires = List.of(1L, 2L, 3L, 4L, 5L); // IDs de los cuestionarios

            if (scoreBasedQuestionnaires.contains(questionnaireId)) {
                // Devolver score total por fecha
                Map<Date, Integer> scores = questionnaireService.getScoreByDate(questionnaireId, patientId);
                return ResponseEntity.ok(scores);
            } else {
                // Devolver respuestas detalladas por pregunta y fecha
                Map<Date, List<CustomResponseDto>> groupedResponses = questionnaireService
                        .getQuestionnaireResponsesGroupedByDate(questionnaireId, patientId);
                return ResponseEntity.ok(groupedResponses);
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // @GetMapping("/{patientId}/completed-questionnaires")
    // Obtener cuestionarios completados por un paciente
    @GetMapping("/patients/{id}/completed")
    public ResponseEntity<List<QuestionnaireResponseDto>> getCompletedQuestionnaires(@PathVariable Long patientId) {
        try {
            List<QuestionnaireResponseDto> completedQuestionnaires = questionnaireService
                    .getCompletedQuestionnaires(patientId);
            return ResponseEntity.ok(completedQuestionnaires);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{patientId}/questionnaires/{questionnaireId}/responses-grouped")
    public ResponseEntity<Map<Date, List<CustomResponseDto>>> getQuestionnaireResponsesGroupedByDate(
            @PathVariable Long patientId,
            @PathVariable Long questionnaireId) {
        try {
            Map<Date, List<CustomResponseDto>> groupedResponses = questionnaireService
                    .getQuestionnaireResponsesGroupedByDate(questionnaireId, patientId);
            return ResponseEntity.ok(groupedResponses);
        } catch (RuntimeException e) {
            log.error("Error fetching grouped responses: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener scores por sesión
    @GetMapping("/{patientId}/questionnaires/{questionnaireId}/scores")
    public ResponseEntity<List<Map<String, Object>>> getScoresBySession(
            @PathVariable Long patientId,
            @PathVariable Long questionnaireId) {
        try {
            List<Map<String, Object>> scoresBySession = questionnaireService.getScoresBySession(questionnaireId,
                    patientId);
            return ResponseEntity.ok(scoresBySession);
        } catch (RuntimeException e) {
            log.error("Error al obtener scores por sesión: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // @PostMapping("/{questionnaireId}/add-comments")
    // Agregar comentarios a un cuestionario
    @PostMapping("/{id}/comments")
    public ResponseEntity<String> addComments(
            @PathVariable("id") Long questionnaireId,
            @RequestParam Long patientId,
            @RequestBody String comments) {
        try {
            questionnaireService.addComments(questionnaireId, patientId, comments);
            return ResponseEntity.ok("Comentarios añadidos y cuestionario marcado como completado.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /*
     * @GetMapping("/{patientId}/in-progress")
     * public ResponseEntity<?> getQuestionnairesInProgressByPatient(@PathVariable
     * Long patientId) {
     * try {
     * List<Map<String, Object>> inProgressQuestionnaires = questionnaireService
     * .getQuestionnairesInProgressByPatient(patientId);
     * return ResponseEntity.ok(inProgressQuestionnaires);
     * } catch (RuntimeException e) {
     * Map<String, String> errorResponse = new HashMap<>();
     * errorResponse.put("message",
     * "No se encontraron cuestionarios en progreso para el paciente.");
     * return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
     * }
     * }
     */

    @GetMapping("/patients/{patientId}/questionnaires")
    public ResponseEntity<?> getQuestionnairesByStatus(
            @PathVariable Long patientId,
            @RequestParam(required = false) String status) {
        try {
            if ("in-progress".equalsIgnoreCase(status)) {
                List<Map<String, Object>> inProgressQuestionnaires = questionnaireService
                        .getQuestionnairesInProgressByPatient(patientId);
                return ResponseEntity.ok(inProgressQuestionnaires);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Estado no soportado o no especificado.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error al recuperar cuestionarios para el paciente.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /*
     * // Endpoint para obtener las respuestas detalladas de un cuestionario
     * 
     * @GetMapping("/{patientId}/{questionnaireId}/detailed-responses")
     * public ResponseEntity<List<Map<String, Object>>>
     * getDetailedQuestionnaireResponses(
     * 
     * @PathVariable Long patientId,
     * 
     * @PathVariable Long questionnaireId) {
     * try {
     * List<Map<String, Object>> responses =
     * questionnaireService.getQuestionnaireResponsesWithDetails(patientId,
     * questionnaireId);
     * return ResponseEntity.ok(responses);
     * } catch (RuntimeException e) {
     * return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
     * }
     * }
     */
    @GetMapping("/patients/{patientId}/questionnaires/{questionnaireId}/responses/details")
    public ResponseEntity<List<Map<String, Object>>> getDetailedQuestionnaireResponses(
            @PathVariable Long patientId,
            @PathVariable Long questionnaireId) {
        try {
            List<Map<String, Object>> responses = questionnaireService.getQuestionnaireResponsesWithDetails(patientId,
                    questionnaireId);
            return ResponseEntity.ok(responses);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error al obtener respuestas detalladas del cuestionario.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener estadísticas de un fisioterapeuta
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics(@RequestParam Long physiotherapistId) {
        try {
            Map<String, Object> stats = questionnaireService.getStatisticsForPhysiotherapist(physiotherapistId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al obtener estadísticas generales"));
        }
    }

    /*
     * @ GetMapping("/completed-by-patient")
     * public ResponseEntity<List<Map<String, Object>>>
     * getCompletedQuestionnairesByPatient(
     * 
     * @RequestParam Long physiotherapistId) {
     * try {
     * List<Map<String, Object>> stats = questionnaireService
     * .getCompletedQuestionnairesByPatientForPhysiotherapist(physiotherapistId);
     * return ResponseEntity.ok(stats);
     * } catch (Exception e) {
     * return ResponseEntity.internalServerError().body(List
     * .of(Map.of("error",
     * "Error al obtener estadísticas de cuestionarios completados por paciente")));
     * }
     * }
     */

    @GetMapping("/completed-questionnaires")
    public ResponseEntity<List<Map<String, Object>>> getCompletedQuestionnairesByPatient(
            @RequestParam Long physiotherapistId) {
        try {
            // Obtener estadísticas desde el servicio
            List<Map<String, Object>> stats = questionnaireService
                    .getCompletedQuestionnairesByPatientForPhysiotherapist(physiotherapistId);

            // Retornar la respuesta exitosa
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            // Manejar excepciones y devolver un error estandarizado
            Map<String, Object> errorResponse = Map.of(
                    "error", "Error al obtener estadísticas de cuestionarios completados por paciente",
                    "details", e.getMessage());
            return ResponseEntity.internalServerError().body(List.of(errorResponse));
        }
    }

    /*
     * @GetMapping("/completion-rates")
     * public ResponseEntity<Map<String, Object>>
     * getCompletionRatesByPatient(@RequestParam Long physiotherapistId) {
     * try {
     * Map<String, Object> stats = questionnaireService
     * .getCompletionRatesByPatientForPhysiotherapist(physiotherapistId);
     * return ResponseEntity.ok(stats);
     * } catch (Exception e) {
     * return ResponseEntity.internalServerError()
     * .body(Map.of("error", "Error al obtener tasas de completados por paciente"));
     * }
     * }
     */
    @GetMapping("/questionnaires/completion-rates/{physiotherapistId}")
    public ResponseEntity<?> getCompletionRatesByPhysiotherapist(
            @PathVariable Long physiotherapistId) {
        try {
            // Llamar al servicio para obtener las tasas de finalización
            Map<String, Object> stats = questionnaireService
                    .getCompletionRatesByPatientForPhysiotherapist(physiotherapistId);

            // Devolver la respuesta exitosa
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            // Registrar el error y devolver detalles en la respuesta
            log.error("Error al obtener tasas de finalización: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Error al obtener tasas de finalización",
                            "details", e.getMessage()));
        } catch (Exception e) {
            // Captura genérica para errores no esperados
            log.error("Excepción inesperada: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Error inesperado al procesar la solicitud",
                            "details", e.getMessage()));
        }
    }

}
