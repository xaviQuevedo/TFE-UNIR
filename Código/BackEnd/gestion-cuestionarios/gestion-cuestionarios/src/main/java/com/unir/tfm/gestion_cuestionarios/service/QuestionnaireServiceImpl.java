package com.unir.tfm.gestion_cuestionarios.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unir.tfm.gestion_cuestionarios.client.UserClient;
import com.unir.tfm.gestion_cuestionarios.data.PatientQuestionnaireRepository;
import com.unir.tfm.gestion_cuestionarios.data.QuestionRepository;
import com.unir.tfm.gestion_cuestionarios.data.QuestionnaireRepository;
import com.unir.tfm.gestion_cuestionarios.data.QuestionnaireResponseRepository;
import com.unir.tfm.gestion_cuestionarios.model.entity.PatientQuestionnaire;
import com.unir.tfm.gestion_cuestionarios.model.entity.Questionnaire;
import com.unir.tfm.gestion_cuestionarios.model.request.AssignQuestionnaireRequest;
import com.unir.tfm.gestion_cuestionarios.model.response.CustomResponseDto;
import com.unir.tfm.gestion_cuestionarios.model.response.QuestionResponseDto;
import com.unir.tfm.gestion_cuestionarios.model.response.QuestionnaireResponse;
import com.unir.tfm.gestion_cuestionarios.model.response.QuestionnaireResponseDto;
import com.unir.tfm.gestion_cuestionarios.model.response.ResponseDto;

import java.util.Comparator;


@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {

        @Autowired
        private QuestionnaireRepository questionnaireRepository;

        @Autowired
        private PatientQuestionnaireRepository patientQuestionnaireRepository;

        @Autowired
        private QuestionnaireResponseRepository questionnaireResponseRepository;

        @Autowired
        private QuestionRepository questionRepository;

        @Autowired
        private UserClient userClient;

        private static final Logger log = LoggerFactory.getLogger(QuestionnaireServiceImpl.class);

        @Override
        public void assignQuestionnaire(AssignQuestionnaireRequest request) {
                // Verificar que el paciente existe
                if (!userClient.userExists(request.getPatientId())) {
                        throw new RuntimeException("User not found");
                }
                // Verificar que el fisioterapeuta existe

                if (!userClient.userExists(request.getPhysiotherapistId())) {
                        throw new RuntimeException("Physiotherapist not found");
                }

                for (Long questionnaireId : request.getQuestionnaireIds()) {
                        // Buscar el cuestionario por su ID
                        Questionnaire questionnaire = questionnaireRepository.findById(questionnaireId)
                                        .orElseThrow(() -> new RuntimeException("Questionnaire not found"));

                        // Crear y guardar la relación con el paciente y el fisioterapeuta
                        PatientQuestionnaire patientQuestionnaire = PatientQuestionnaire.builder()
                                        .patientId(request.getPatientId()) // ID del paciente
                                        .physiotherapistId(request.getPhysiotherapistId()) // ID del fisioterapeuta
                                        .questionnaire(questionnaire) // Cuestionario asignado
                                        .status("pending") // Estado inicial
                                        .assignedAt(LocalDateTime.now()) // Fecha de asignación
                                        .build();

                        patientQuestionnaireRepository.save(patientQuestionnaire);
                }
        }

        @Override
public void submitAnswerAndUpdateStatus(Long questionnaireId, Long patientId, Map<String, List<ResponseDto>> responses) {
    log.info("Procesando respuestas. Patient ID: {}, Questionnaire ID: {}", patientId, questionnaireId);

    // Obtener las respuestas de la solicitud
    List<ResponseDto> responseList = responses.get("responses");
    if (responseList == null || responseList.isEmpty()) {
        throw new RuntimeException("No se encontraron respuestas en la solicitud");
    }

    log.info("Respuestas recibidas: {}", responseList);

    // Validar que el cuestionario esté asignado al paciente (pueden haber múltiples registros)
    List<PatientQuestionnaire> patientQuestionnaires = patientQuestionnaireRepository
            .findByPatientIdAndQuestionnaireId(patientId, questionnaireId);

    if (patientQuestionnaires.isEmpty()) {
        throw new RuntimeException("Cuestionario no asignado encontrado");
    }

    // Seleccionar el registro más reciente (o aplicar lógica de negocio específica)
    PatientQuestionnaire patientQuestionnaire = patientQuestionnaires.stream()
            .max(Comparator.comparing(PatientQuestionnaire::getAssignedAt)) // Elegir por fecha de asignación más reciente
            .orElseThrow(() -> new RuntimeException("No se encontró un cuestionario válido"));

    log.info("Cuestionario asignado encontrado: {}", patientQuestionnaire);

    // Guardar las respuestas
    for (ResponseDto response : responseList) {
        QuestionnaireResponse responseEntity = new QuestionnaireResponse();
        responseEntity.setQuestionnaireId(questionnaireId);
        responseEntity.setQuestionId(response.getQuestionId());
        responseEntity.setPatientId(patientId);
        responseEntity.setAnswer(response.getAnswer());
        responseEntity.setCreatedAt(new Date());
        questionnaireResponseRepository.save(responseEntity);

        log.info("Respuesta guardada: {}", responseEntity);
    }

    // Actualizar estado del cuestionario asignado
    patientQuestionnaire.setStatus("completed");
    patientQuestionnaire.setUpdatedAt(new Date());
    patientQuestionnaireRepository.save(patientQuestionnaire);

    log.info("Estado del cuestionario actualizado a 'completed'");
}

        @Override
        public QuestionnaireResponseDto getQuestionnaire(Long questionnaireId) {
                // Buscar el cuestionario
                Questionnaire questionnaire = questionnaireRepository.findById(questionnaireId)
                                .orElseThrow(() -> new RuntimeException("Questionnaire not found"));

                // Obtener las preguntas asociadas
                List<QuestionResponseDto> questions = questionRepository.findByQuestionnaireId(questionnaireId).stream()
                                .map(q -> new QuestionResponseDto(q.getId(), q.getText(), q.getType(), q.getOptions()))
                                .collect(Collectors.toList()); // Asegúrate de usar Collectors.toList() si tu versión de
                                                               // Java es anterior a la 16

                // Crear y devolver el DTO del cuestionario
                return new QuestionnaireResponseDto(
                                questionnaire.getId(),
                                questionnaire.getTitle(),
                                questionnaire.getDescription(),
                                questions);
        }

        @Override
        public List<QuestionnaireResponseDto> getAvailableQuestionnaires(Long patientId) {

                // Obtener cuestionarios asignados al paciente con estado "pending"
                List<Questionnaire> pendingQuestionnaires = patientQuestionnaireRepository
                                .findByPatientIdAndStatus(patientId, "pending")
                                .stream()
                                .map(PatientQuestionnaire::getQuestionnaire) // Mapeo al objeto Questionnaire
                                .collect(Collectors.toList());

                // Mapear los cuestionarios a DTOs
                return pendingQuestionnaires.stream()
                                .map(q -> new QuestionnaireResponseDto(
                                                q.getId(),
                                                q.getTitle(),
                                                q.getDescription(),
                                                null)) // null para preguntas si no son necesarias aquí
                                .collect(Collectors.toList());
        }

        // metodo a mantener y funcionando correctamente
        @Override
        public List<QuestionnaireResponseDto> getNotAssignedQuestionnaires(Long patientId) {
                // Obtener IDs de cuestionarios asignados al paciente con estado "completed"
                List<Long> assignedCompletedQuestionnaireIds = patientQuestionnaireRepository
                                .findByPatientIdAndStatus(patientId, "completed")
                                .stream()
                                .map(pq -> pq.getQuestionnaire().getId())
                                .collect(Collectors.toList());

                // Obtener IDs de cuestionarios asignados al paciente con estado "pending"
                List<Long> assignedPendingQuestionnaireIds = patientQuestionnaireRepository
                                .findByPatientIdAndStatus(patientId, "pending")
                                .stream()
                                .map(pq -> pq.getQuestionnaire().getId())
                                .collect(Collectors.toList());

                // Filtrar cuestionarios no asignados o con estado "completed"
                List<Questionnaire> notAssignedQuestionnaires = questionnaireRepository.findAll()
                                .stream()
                                .filter(q -> !assignedPendingQuestionnaireIds.contains(q.getId()) // Excluir pendientes
                                                && (!assignedCompletedQuestionnaireIds.contains(q.getId()) // Incluir no
                                                                                                           // asignados
                                                                || assignedCompletedQuestionnaireIds
                                                                                .contains(q.getId()))) // Incluir
                                                                                                       // completados
                                .collect(Collectors.toList());

                // Mapear a DTOs
                return notAssignedQuestionnaires.stream()
                                .map(q -> new QuestionnaireResponseDto(
                                                q.getId(),
                                                q.getTitle(),
                                                q.getDescription(),
                                                null)) // Excluir preguntas en esta respuesta
                                .collect(Collectors.toList());
        }

        @Override
        public QuestionnaireResponseDto getQuestionnaireWithQuestions(Long questionnaireId) {
                Questionnaire questionnaire = questionnaireRepository.findById(questionnaireId)
                                .orElseThrow(() -> new RuntimeException("Questionnaire not found"));

                ObjectMapper objectMapper = new ObjectMapper();

                List<QuestionResponseDto> questions = questionRepository.findByQuestionnaireId(questionnaireId).stream()
                                .map(question -> {
                                        Object parsedOptions = null;
                                        try {
                                                if (question.getOptions() != null && !question.getOptions().isEmpty()) {
                                                        String optionsAsString = question.getOptions().trim();

                                                        // Caso 1: JSON Mapa ({"key": "value", ...})
                                                        if (optionsAsString.startsWith("{")
                                                                        && optionsAsString.contains(":")) {
                                                                parsedOptions = objectMapper.readValue(
                                                                                optionsAsString,
                                                                                new TypeReference<Map<String, String>>() {
                                                                                });
                                                        }
                                                        // Caso 2: JSON Lista (["value1", "value2", ...])
                                                        else if (optionsAsString.startsWith("[")
                                                                        && optionsAsString.endsWith("]")) {
                                                                parsedOptions = objectMapper.readValue(
                                                                                optionsAsString,
                                                                                new TypeReference<List<String>>() {
                                                                                });
                                                        }
                                                }
                                        } catch (Exception e) {
                                                throw new RuntimeException(
                                                                "Error al procesar opciones de la pregunta con ID: "
                                                                                + question.getId(),
                                                                e);
                                        }

                                        return new QuestionResponseDto(
                                                        question.getId(),
                                                        question.getText(),
                                                        question.getType(),
                                                        parsedOptions);
                                })
                                .collect(Collectors.toList());

                return new QuestionnaireResponseDto(
                                questionnaire.getId(),
                                questionnaire.getTitle(),
                                questionnaire.getDescription(),
                                questions);
        }

        @Override
        public double calculateProgress(Long patientId, long questionnaireId) {
                // Obtener respuestas del cuestionario asignado al paciente
                List<QuestionnaireResponse> responses = questionnaireResponseRepository
                                .findByPatientIdAndQuestionnaireIdOrderByCreatedAtAsc(patientId, questionnaireId);

                if (responses.isEmpty()) {
                        throw new RuntimeException("No se encontraron respuestas para el cuestionario");
                }

                // Agrupar respuestas por fecha
                Map<Date, List<QuestionnaireResponse>> groupedByDate = responses.stream()
                                .collect(Collectors.groupingBy(QuestionnaireResponse::getCreatedAt));

                // Obtener la fecha más antigua y la más reciente
                Date initialDate = Collections.min(groupedByDate.keySet());
                Date recentDate = Collections.max(groupedByDate.keySet());

                List<QuestionnaireResponse> initialResponses = groupedByDate.get(initialDate);
                List<QuestionnaireResponse> recentResponses = groupedByDate.get(recentDate);

                // Calcular las puntuaciones totales iniciales y recientes
                int initialScore = initialResponses.stream()
                                .mapToInt(r -> Integer.parseInt(r.getAnswer()))
                                .sum();

                int recentScore = recentResponses.stream()
                                .mapToInt(r -> Integer.parseInt(r.getAnswer()))
                                .sum();

                // Calcular el porcentaje de progreso
                return initialScore == 0 ? 0 : ((recentScore - initialScore) / (double) initialScore) * 100;
        }

        @Override
        public Map<String, List<QuestionnaireResponse>> getQuestionnaireResponsesGroupedByQuestion(Long questionnaireId,
                Long patientId) {
            // Validar que el cuestionario esté asignado al paciente
            List<PatientQuestionnaire> patientQuestionnaires = patientQuestionnaireRepository
                    .findByPatientIdAndQuestionnaireId(patientId, questionnaireId);
        
            if (patientQuestionnaires.isEmpty()) {
                throw new RuntimeException("Cuestionario no asignado encontrado");
            }
        
            // Obtener todas las respuestas del cuestionario para el paciente
            List<QuestionnaireResponse> responses = questionnaireResponseRepository
                    .findByPatientIdAndQuestionnaireIdOrderByCreatedAtAsc(patientId, questionnaireId);
        
            if (responses.isEmpty()) {
                throw new RuntimeException("No se encontraron respuestas para el cuestionario");
            }
        
            // Agrupar respuestas por pregunta
            return responses.stream()
                    .collect(Collectors.groupingBy(r -> "Pregunta ID: " + r.getQuestionId()));
        }
        

        @Override
public List<QuestionnaireResponseDto> getCompletedQuestionnaires(Long patientId) {
    // Obtener todos los cuestionarios completados
    List<Questionnaire> completedQuestionnaires = patientQuestionnaireRepository
            .findByPatientIdAndStatus(patientId, "completed")
            .stream()
            .map(PatientQuestionnaire::getQuestionnaire)
            .collect(Collectors.toList());

    // Filtrar cuestionarios únicos por su ID
    Map<Long, Questionnaire> uniqueQuestionnaires = completedQuestionnaires.stream()
            .collect(Collectors.toMap(Questionnaire::getId, q -> q, (existing, replacement) -> existing));

    // Convertir los cuestionarios únicos a DTOs
    return uniqueQuestionnaires.values().stream()
            .map(q -> new QuestionnaireResponseDto(q.getId(), q.getTitle(), q.getDescription(), null))
            .collect(Collectors.toList());
}


        @Override
        public Map<Date, List<CustomResponseDto>> getQuestionnaireResponsesGroupedByDate(Long questionnaireId,
                        Long patientId) {
                List<CustomResponseDto> responses = questionnaireResponseRepository.findCustomResponses(patientId,
                                questionnaireId);

                if (responses.isEmpty()) {
                        throw new RuntimeException("No se encontraron respuestas para el cuestionario.");
                }

                // Agrupar respuestas por fecha
                return responses.stream()
                                .collect(Collectors.groupingBy(CustomResponseDto::getCreatedAt));
        }

        @Override
        public Map<Date, Integer> getScoreByDate(Long questionnaireId, Long patientId) {
                List<QuestionnaireResponse> responses = questionnaireResponseRepository
                                .findByPatientIdAndQuestionnaireIdOrderByCreatedAtAsc(patientId, questionnaireId);

                if (responses.isEmpty()) {
                        throw new RuntimeException("No se encontraron respuestas para el cuestionario.");
                }

                // Agrupar respuestas por fecha y calcular el score total
                return responses.stream()
                                .collect(Collectors.groupingBy(
                                                QuestionnaireResponse::getCreatedAt,
                                                Collectors.summingInt(
                                                                response -> Integer.parseInt(response.getAnswer()))));
        }

        @Override
public List<Map<String, Object>> getScoresBySession(Long questionnaireId, Long patientId) {
    // Obtener todas las respuestas del cuestionario para el paciente
    List<QuestionnaireResponse> responses = questionnaireResponseRepository
            .findByPatientIdAndQuestionnaireIdOrderByCreatedAt(patientId, questionnaireId);

    if (responses.isEmpty()) {
        throw new RuntimeException("No se encontraron respuestas para el cuestionario.");
    }

    // Agrupar respuestas por día (ignorar tiempo)
    Map<LocalDate, List<QuestionnaireResponse>> groupedByDate = responses.stream()
            .collect(Collectors.groupingBy(response -> 
                response.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            ));

    // Calcular un score por cada día
    List<Map<String, Object>> scoresBySession = groupedByDate.entrySet().stream()
            .map(entry -> {
                LocalDate sessionDate = entry.getKey();
                List<QuestionnaireResponse> sessionResponses = entry.getValue();

                // Calcular el score sumando las respuestas numéricas
                int score = sessionResponses.stream()
                        .mapToInt(response -> {
                            try {
                                return Integer.parseInt(response.getAnswer());
                            } catch (NumberFormatException e) {
                                return 0; // Ignorar respuestas no numéricas
                            }
                        })
                        .sum();

                // Crear un objeto con la fecha y el score
                Map<String, Object> sessionScore = new HashMap<>();
                sessionScore.put("date", sessionDate);
                sessionScore.put("score", score);
                return sessionScore;
            })
            .sorted(Comparator.comparing(a -> (LocalDate) a.get("date"))) // Ordenar por fecha
            .collect(Collectors.toList());

    return scoresBySession;
}


}
