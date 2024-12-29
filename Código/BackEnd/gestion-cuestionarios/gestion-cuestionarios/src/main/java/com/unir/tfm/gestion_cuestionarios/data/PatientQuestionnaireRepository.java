package com.unir.tfm.gestion_cuestionarios.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unir.tfm.gestion_cuestionarios.model.entity.PatientQuestionnaire;

@Repository
public interface PatientQuestionnaireRepository extends JpaRepository<PatientQuestionnaire, Long> {
    List<PatientQuestionnaire> findByPatientIdAndStatus(Long patientId, String status);

    List<PatientQuestionnaire> findByPatientIdAndQuestionnaireId(Long patientId, Long questionnaireId);

    List<PatientQuestionnaire> findByPatientId(Long patientId);

}
