package com.unir.tfm.gestion_cuestionarios.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unir.tfm.gestion_cuestionarios.model.entity.PatientQuestionnaire;

public interface PatientQuestionnaireRepository extends JpaRepository<PatientQuestionnaire, Long> {

}
