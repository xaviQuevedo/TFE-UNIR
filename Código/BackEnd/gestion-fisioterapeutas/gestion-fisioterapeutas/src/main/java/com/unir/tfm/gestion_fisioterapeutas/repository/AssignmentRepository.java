package com.unir.tfm.gestion_fisioterapeutas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unir.tfm.gestion_fisioterapeutas.model.Assignment;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    /**
     * Busca todas las asignaciones para un fisioterapeuta específico.
     *
     * @param physiotherapistId ID del fisioterapeuta.
     * @return Lista de asignaciones del fisioterapeuta.
     */
    List<Assignment> findByPhysiotherapistId(Long physiotherapistId);

    /**
     * Verifica si ya existe una asignación entre un fisioterapeuta y un paciente.
     *
     * @param physiotherapistId ID del fisioterapeuta.
     * @param patientId         ID del paciente.
     * @return true si existe la asignación; false en caso contrario.
     */
    boolean existsByPatientIdAndPhysiotherapistId(Long physiotherapistId, Long patientId);
}
