package com.unir.tfm.gestion_fisioterapeutas.service;

import com.unir.tfm.gestion_fisioterapeutas.facade.UsersFacade;
import com.unir.tfm.gestion_fisioterapeutas.model.Assignment;
import com.unir.tfm.gestion_fisioterapeutas.model.User;
import com.unir.tfm.gestion_fisioterapeutas.repository.AssignmentRepository;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    // Inyección por constructor
    private final AssignmentRepository assignmentRepository;
    private final UsersFacade usersFacade;

    // private static final Logger logger =
    // LoggerFactory.getLogger(AssignmentServiceImpl.class);
    public String obtenerTokenActual() {
        System.out.println("Obteniendo token actual");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            // System.out.println("El contexto de seguridad no contiene autenticación.");
            throw new IllegalStateException("El contexto de seguridad no contiene autenticación.");
        }

        // System.out.println("Tipo de autenticación: " +
        // authentication.getClass().getName());
        // System.out.println("Detalles de autenticación: " + authentication);

        if (authentication instanceof UsernamePasswordAuthenticationToken authToken) {
            Object details = authToken.getDetails();

            if (details instanceof String token) {
                // System.out.println("Token encontrado en el contexto de seguridad: " + token);
                return token;
            } else {
                System.out.println("Los detalles de autenticación no contienen el token esperado.");
                throw new IllegalStateException("Los detalles de autenticación no contienen el token.");
            }
        }

        throw new IllegalStateException("La autenticación no es de tipo UsernamePasswordAuthenticationToken.");
    }

    @Override
    public List<Assignment> assignPatientsToPhysiotherapist(List<Long> patientIds, Long physiotherapistId) {
        System.out.println("Entrando a assignPatientsToPhysiotherapist");

        // Obtener el token JWT del contexto de seguridad
        String token = obtenerTokenActual();
        if (token == null) {
            throw new IllegalArgumentException("Token no encontrado o inválido");
        }
        System.out.println("Token: " + token);

        // Obtener los detalles del fisioterapeuta
        User physiotherapist = usersFacade.getUser(physiotherapistId, token);
        if (physiotherapist == null || !"physiotherapist".equals(physiotherapist.getRole())) {
            throw new IllegalArgumentException(
                    "El usuario con ID " + physiotherapistId + " no es un fisioterapeuta válido.");
        }

        // Lista para almacenar las asignaciones realizadas
        List<Assignment> assignments = new ArrayList<>();

        for (Long patientId : patientIds) {
            // Obtener los detalles del paciente
            User patient = usersFacade.getUser(patientId, token);
            if (patient == null || !"patient".equals(patient.getRole())) {
                throw new IllegalArgumentException("El usuario con ID " + patientId + " no es un paciente válido.");
            }

            // Verificar si ya existe una asignación para este paciente y fisioterapeuta
            // usando el método existsAssignment
            if (existsAssignment(patientId, physiotherapistId)) {
                throw new IllegalArgumentException(
                        "Ya existe una asignación entre el paciente con ID " + patientId +
                                " y el fisioterapeuta con ID " + physiotherapistId);
            }

            // Crear y guardar la asignación
            Assignment assignment = new Assignment();
            assignment.setPhysiotherapistId(physiotherapistId);
            assignment.setPatientId(patientId);

            assignments.add(assignmentRepository.save(assignment));
        }

        // Retornar todas las asignaciones realizadas
        return assignments;
    }

    @Override
    public List<User> getAssignedPatients(Long physiotherapistId) {
        // Obtener todas las asignaciones por el ID del fisioterapeuta
        List<Assignment> assignments = assignmentRepository.findByPhysiotherapistId(physiotherapistId);

        // Obtener los IDs de los pacientes asignados
        List<Long> patientIds = assignments.stream()
                .map(Assignment::getPatientId)
                .distinct()
                .collect(Collectors.toList());

        // Llamar al microservicio de usuarios para obtener los datos completos de los
        // pacientes
        String token = obtenerTokenActual();
        return patientIds.stream()
                .map(patientId -> usersFacade.getUser(patientId, token))
                .filter(user -> user != null) // Asegúrate de excluir nulos
                .collect(Collectors.toList());
    }

    @Override
    public boolean isUserValid(Long userId, String expectedRole) {
        String token = obtenerTokenActual();
        User user = usersFacade.getUser(userId, token);
        if (user == null) {
            System.out.println("User not found for ID: " + userId);
            return false;
        }
        System.out.println("Rolee ms-GF: " + user.getRole());
        return expectedRole.equalsIgnoreCase(user.getRole());
    }

    /*
     * @Override
     * public boolean isUserValid(Long userId, String expectedRole) {
     * User user = usersFacade.getUser(userId);
     * 
     * System.out.println("Rolee ms-GF" + user.getRole());
     * return user !=null && expectedRole.equalsIgnoreCase(user.getRole());
     * }
     */
    @Override
    public List<Assignment> getAssignmentsByPhysiotherapist(Long physiotherapistId) {
        return assignmentRepository.findByPhysiotherapistId(physiotherapistId);
    }

    @Override
    public List<User> getUnassignedPatients(Long physiotherapistId) {
        // Obtener todos los pacientes desde el microservicio de usuarios
        List<User> allPatients = getPatients(); // Reutilizamos el método ya existente
        // Obtener los IDs de pacientes ya asignados a cualquier fisioterapeuta
        List<Long> assignedPatientIds = assignmentRepository.findAll().stream()
                .map(Assignment::getPatientId)
                .distinct()
                .collect(Collectors.toList());

        // Filtrar pacientes no asignados
        return allPatients.stream()
                .filter(patient -> !assignedPatientIds.contains(patient.getUser_id())) // Asegúrate de usar el getter
                                                                                       // correcto
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsAssignment(Long patientId, Long physiotherapistId) {
        return assignmentRepository.existsByPatientIdAndPhysiotherapistId(patientId, physiotherapistId);
    }

    @Override
    public List<User> getPatients() {
        String token = obtenerTokenActual();
        return usersFacade.getUsersByRole("patient", token);
    }

    @Override
    public List<User> getPhysiotherapists() {
        String token = obtenerTokenActual();
        return usersFacade.getUsersByRole("physiotherapist", token);
    }

    @Override
    public void ensureAllPhysiotherapistsHavePatients() {
        List<User> physiotherapists = getPhysiotherapists();
        physiotherapists.forEach(physiotherapist -> {
            Long physiotherapistId = physiotherapist.getUser_id();
            if (assignmentRepository.findByPhysiotherapistId(physiotherapistId).isEmpty()) {
                throw new IllegalStateException(
                        String.format("Physiotherapist with ID %d has no assigned patients.", physiotherapistId));
            }
        });
    }

}
