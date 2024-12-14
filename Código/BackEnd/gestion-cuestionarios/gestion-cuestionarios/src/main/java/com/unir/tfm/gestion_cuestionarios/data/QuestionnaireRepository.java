package com.unir.tfm.gestion_cuestionarios.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unir.tfm.gestion_cuestionarios.model.entity.Questionnaire;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {

}
