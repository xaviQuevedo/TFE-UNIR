package com.unir.tfm.gestion_cuestionarios.data;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unir.tfm.gestion_cuestionarios.model.entity.PatientQuestionnaire;

@Repository
public interface PatientQuestionnaireRepository extends JpaRepository<PatientQuestionnaire, Long> {
    List<PatientQuestionnaire> findByPatientIdAndStatus(Long patientId, String status);

    List<PatientQuestionnaire> findByPatientIdAndQuestionnaireId(Long patientId, Long questionnaireId);

    List<PatientQuestionnaire> findByPatientId(Long patientId);

    @Query("SELECT COUNT(DISTINCT pq.patientId) FROM PatientQuestionnaire pq WHERE pq.physiotherapistId = :physiotherapistId")
    long countDistinctPatientsByPhysiotherapist(@Param("physiotherapistId") Long physiotherapistId);

    @Query("SELECT COUNT(pq) FROM PatientQuestionnaire pq WHERE pq.status = :status AND pq.physiotherapistId = :physiotherapistId")
    long countByStatusAndPhysiotherapist(@Param("status") String status,
            @Param("physiotherapistId") Long physiotherapistId);

    @Query(value = """
            SELECT
                pq.patient_id AS patientId,
                COUNT(*) AS completedCount
            FROM
                patient_questionnaires pq
            WHERE
                pq.status = 'completed' AND pq.physiotherapist_id = :physiotherapistId
            GROUP BY
                pq.patient_id
            """, nativeQuery = true)
    List<Map<String, Object>> getCompletedQuestionnairesByPatientForPhysiotherapist(
            @Param("physiotherapistId") Long physiotherapistId);

    @Query(value = """
            SELECT
                pq.patient_id AS patientId,
                COUNT(*) AS pendingCount
            FROM
                patient_questionnaires pq
            WHERE
                pq.physiotherapist_id = :physiotherapistId
                AND pq.status = 'pending'
            GROUP BY
                pq.patient_id
            """, nativeQuery = true)
    List<Map<String, Object>> getPendingQuestionnairesByPatientForPhysiotherapist(
            @Param("physiotherapistId") Long physiotherapistId);

}
