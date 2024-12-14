package com.unir.tfm.gestion_fisioterapeutas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unir.tfm.gestion_fisioterapeutas.model.Assignment;
import com.unir.tfm.gestion_fisioterapeutas.service.AssignmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    // EndPoint para asignar un paciente a un fisioterapeuta
    @PostMapping("/assign")
    public ResponseEntity<?> assignPatientToPhysiotherapist(
        @RequestParam Long patientId,
        @RequestParam Long physiotherapistId) {

    System.out.println("Entrando a /assign endpoint");

    try {
        // Validar si los IDs son válidos
        if (!assignmentService.isUserValid(patientId, "patient") || !assignmentService.isUserValid(physiotherapistId, "physiotherapist")) {
            return ResponseEntity.badRequest().body("Invalid patient or physiotherapist ID");
        }

        Assignment assignment = assignmentService.assignPatientToPhysiotherapist(patientId, physiotherapistId);
        return ResponseEntity.ok(assignment);
    } catch (IllegalArgumentException e) {
        // Responder con un mensaje de error en caso de conflictos como asignación duplicada
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        // Manejar errores no previstos
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


    @GetMapping("/patients")
    public ResponseEntity<List<?>> getPatients() {
        return ResponseEntity.ok(assignmentService.getPatients());
    }

    @GetMapping("/physiotherapists")
    public ResponseEntity<List<?>> getPhysiotherapists() {
        return ResponseEntity.ok(assignmentService.getPhysiotherapists());
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


}
