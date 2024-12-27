package com.unir.tfm.gestion_cuestionarios.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unir.tfm.gestion_cuestionarios.model.response.CustomResponseDto;
import com.unir.tfm.gestion_cuestionarios.model.response.QuestionnaireResponse;

public interface QuestionnaireResponseRepository extends JpaRepository<QuestionnaireResponse, Long> {
    List<QuestionnaireResponse> findByPatientIdAndQuestionnaireIdOrderByCreatedAtAsc(Long patientId,
            Long questionnaireId);

    @Query("SELECT new com.unir.tfm.gestion_cuestionarios.model.response.CustomResponseDto(qr.createdAt, q.text, qr.answer) "
            +
            "FROM QuestionnaireResponse qr " +
            "JOIN Question q ON qr.questionId = q.id " +
            "WHERE qr.patientId = :patientId AND qr.questionnaireId = :questionnaireId " +
            "ORDER BY qr.createdAt")
    List<CustomResponseDto> findCustomResponses(@Param("patientId") Long patientId,
            @Param("questionnaireId") Long questionnaireId);

    @Query("SELECT qr FROM QuestionnaireResponse qr WHERE qr.patientId = :patientId AND qr.questionnaireId = :questionnaireId")
    List<QuestionnaireResponse> findResponsesForScore(@Param("patientId") Long patientId,
            @Param("questionnaireId") Long questionnaireId);

    @Query("SELECT qr FROM QuestionnaireResponse qr WHERE qr.patientId = :patientId AND qr.questionnaireId = :questionnaireId ORDER BY qr.createdAt")
    List<QuestionnaireResponse> findByPatientIdAndQuestionnaireIdOrderByCreatedAt(
            @Param("patientId") Long patientId,
            @Param("questionnaireId") Long questionnaireId);

}
