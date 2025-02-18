package com.unir.tfm.gestion_fisioterapeutas.service;

import java.util.List;

import com.unir.tfm.gestion_fisioterapeutas.model.Assignment;
import com.unir.tfm.gestion_fisioterapeutas.model.User;

public interface AssignmentService {
    List<Assignment> assignPatientsToPhysiotherapist(List<Long> patientIds, Long physiotherapistId);

    List<User> getAssignedPatients(Long physiotherapistId);

    List<User> getUnassignedPatients(Long physiotherapistId);

    boolean existsAssignment(Long patientId, Long physiotherapistId);

    List<User> getPatients();

    List<User> getPhysiotherapists();

}
