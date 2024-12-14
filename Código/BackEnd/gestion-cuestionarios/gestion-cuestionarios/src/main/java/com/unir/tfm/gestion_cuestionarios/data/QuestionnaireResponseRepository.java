package com.unir.tfm.gestion_cuestionarios.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unir.tfm.gestion_cuestionarios.model.response.QuestionnaireResponse;

public interface QuestionnaireResponseRepository extends JpaRepository<QuestionnaireResponse, Long> {

}
