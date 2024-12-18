package com.unir.tfm.gestion_cuestionarios.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unir.tfm.gestion_cuestionarios.model.entity.PatientQuestionnaire;

public interface PatientQuestionnaireRepository extends JpaRepository<PatientQuestionnaire, Long> {
    List<PatientQuestionnaire> findByPatientIdAndStatus(Long patientId, String status);

}
