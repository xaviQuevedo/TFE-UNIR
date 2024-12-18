package com.unir.tfm.gestion_cuestionarios.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "patient_questionnaires")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientQuestionnaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable =false)
    private Long patientId;

    @Column(name = "physiotherapist_id", nullable = false)
    private Long physiotherapistId;

    @ManyToOne
    @JoinColumn(name = "questionnaire_id", nullable = false)
    private Questionnaire questionnaire;

    @Builder.Default
    @Column(name = "status", nullable = false)
    private String status = "pending";

    @Builder.Default
    @Column(name = "assigned_at", nullable = false, updatable = false)
    private LocalDateTime assignedAt = LocalDateTime.now();

}
