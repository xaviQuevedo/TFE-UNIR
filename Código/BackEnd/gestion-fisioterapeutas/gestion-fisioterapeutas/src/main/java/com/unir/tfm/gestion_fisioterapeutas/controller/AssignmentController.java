package com.unir.tfm.gestion_fisioterapeutas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unir.tfm.gestion_fisioterapeutas.model.AssignRequest;
import com.unir.tfm.gestion_fisioterapeutas.model.Assignment;
import com.unir.tfm.gestion_fisioterapeutas.model.User;
import com.unir.tfm.gestion_fisioterapeutas.service.AssignmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    // EndPoint para asignar un paciente a un fisioterapeuta
    @PostMapping("/assign")
    public ResponseEntity<?> assignPatientToPhysiotherapist(@RequestBody AssignRequest request) {
        try {
            List<Assignment> assignments = assignmentService.assignPatientsToPhysiotherapist(
                    request.getPatientIds(),
                    request.getPhysiotherapistId());

            return ResponseEntity.ok("Patients assigned successfully to physiotherapist. Assignments: " + assignments);
        } catch (IllegalArgumentException e) {
            // Manejar el error de asignación ya existente
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    // EndPoint para obtener los pacientes asignados a un fisioterapeuta
    @GetMapping("/pysiotherapist/{physiotherapistId}")
    public ResponseEntity<List<Assignment>> getAssignedPatients(@RequestParam Long physiotherapistId) {
        List<Assignment> assignments = assignmentService.getAssignmentsByPhysiotherapist(physiotherapistId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsAssignment(
            @RequestParam Long patientId,
            @RequestParam Long physiotherapistId) {
        boolean exists = assignmentService.existsAssignment(patientId, physiotherapistId);
        return ResponseEntity.ok(exists);
    }

    //
    @GetMapping("/patients")
    public ResponseEntity<List<?>> getPatients() {
        List<?> patients = assignmentService.getPatients();
        System.out.println("Patients: " + patients);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/physiotherapists")
    public ResponseEntity<List<?>> getPhysiotherapists() {
        List<?> physiotherapists = assignmentService.getPhysiotherapists();
        return ResponseEntity.ok(physiotherapists);
    }

    @GetMapping("/checkPhysiotherapists")
    public ResponseEntity<?> ensureAllPhysiotherapistsHavePatients() {
        try {
            assignmentService.ensureAllPhysiotherapistsHavePatients();
            return ResponseEntity.ok("All physiotherapists have assigned patients.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/unassigned-patients")
    public ResponseEntity<List<User>> getUnassignedPatients(@RequestParam Long physiotherapistId) {
        try {
            List<User> unassignedPatients = assignmentService.getUnassignedPatients(physiotherapistId);
            return ResponseEntity.ok(unassignedPatients);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

}
