package com.unir.tfm.gestion_cuestionarios.model.response;

import java.time.LocalDateTime;

import com.unir.tfm.gestion_cuestionarios.model.entity.PatientQuestionnaire;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "questionnaire_responses")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionnaireResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_questionnaire_id", nullable = false)
    private PatientQuestionnaire patientQuestionnaire;

    @Column(nullable = false, columnDefinition = "jsonb")
    private String response;

    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completedAt;

}
