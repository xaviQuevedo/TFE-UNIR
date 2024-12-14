package com.unir.tfm.gestion_cuestionarios.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unir.tfm.gestion_cuestionarios.client.UserClient;
import com.unir.tfm.gestion_cuestionarios.data.PatientQuestionnaireRepository;
import com.unir.tfm.gestion_cuestionarios.data.QuestionRepository;
import com.unir.tfm.gestion_cuestionarios.data.QuestionnaireRepository;
import com.unir.tfm.gestion_cuestionarios.data.QuestionnaireResponseRepository;
import com.unir.tfm.gestion_cuestionarios.model.entity.PatientQuestionnaire;
import com.unir.tfm.gestion_cuestionarios.model.entity.Questionnaire;
import com.unir.tfm.gestion_cuestionarios.model.request.AssignQuestionnaireRequest;
import com.unir.tfm.gestion_cuestionarios.model.request.SubmitAnswerRequest;
import com.unir.tfm.gestion_cuestionarios.model.response.QuestionResponseDto;
import com.unir.tfm.gestion_cuestionarios.model.response.QuestionnaireResponse;
import com.unir.tfm.gestion_cuestionarios.model.response.QuestionnaireResponseDto;

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

    @Override
    public void assignQuestionnaire(AssignQuestionnaireRequest request) {
         // Verificar que el usuario existe
         if (!userClient.userExists(request.getUserId())) {
             throw new RuntimeException("User not found");
         }

        // Buscar el cuestionario por su ID
        Questionnaire questionnaire = questionnaireRepository.findById(request.getQuestionnaireId())
                .orElseThrow(() -> new RuntimeException("Questionnaire not found"));

        // Crear y guardar la relación con el paciente
        PatientQuestionnaire patientQuestionnaire = new PatientQuestionnaire();
        patientQuestionnaire.setUserId(request.getUserId());
        patientQuestionnaire.setQuestionnaire(questionnaire);
        patientQuestionnaire.setStatus("pending");

        patientQuestionnaireRepository.save(patientQuestionnaire);
    }

    @Override
    public void submitAnswer(SubmitAnswerRequest request) {
        // Implementar la lógica para guardar respuestas

        // Relacion de cuestionario con paciente
        PatientQuestionnaire patientQuestionnaire = patientQuestionnaireRepository.findById(request.getPatientQuestionnaireId())
                .orElseThrow(() -> new RuntimeException("Patient questionnaire not found"));

        // Se crea la nueva respuesta
        QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
        questionnaireResponse.setPatientQuestionnaire(patientQuestionnaire);
        questionnaireResponse.setResponse(request.getResponse());
        questionnaireResponse.setCompletedAt(request.getCompletedAt());

        // Se guarda la respuesta
        questionnaireResponseRepository.save(questionnaireResponse);

    }

    @Override
public QuestionnaireResponseDto getQuestionnaire(Long questionnaireId) {
    // Buscar el cuestionario
    Questionnaire questionnaire = questionnaireRepository.findById(questionnaireId)
            .orElseThrow(() -> new RuntimeException("Questionnaire not found"));

    // Obtener las preguntas asociadas
    List<QuestionResponseDto> questions = questionRepository.findByQuestionnaireId(questionnaireId).stream()
            .map(q -> new QuestionResponseDto(q.getId(), q.getText(), q.getType(), q.getOptions()))
            .collect(Collectors.toList()); // Asegúrate de usar Collectors.toList() si tu versión de Java es anterior a la 16

    // Crear y devolver el DTO del cuestionario
    return new QuestionnaireResponseDto(
            questionnaire.getId(),
            questionnaire.getTitle(),
            questionnaire.getDescription(),
            questions
    );
}

    
}
