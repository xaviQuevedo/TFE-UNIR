package com.unir.tfm.gestion_cuestionarios.model.response;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.util.Date;

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

    @Column(name = "questionnaire_id", nullable = false)
    private Long questionnaireId;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "physiotherapist_id")
    private Long physiotherapistId;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

}

