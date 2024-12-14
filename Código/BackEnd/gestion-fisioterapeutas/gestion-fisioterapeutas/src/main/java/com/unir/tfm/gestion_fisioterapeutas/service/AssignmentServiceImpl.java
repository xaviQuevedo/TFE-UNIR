package com.unir.tfm.gestion_fisioterapeutas.service;

import com.unir.tfm.gestion_fisioterapeutas.facade.UsersFacade;
import com.unir.tfm.gestion_fisioterapeutas.model.Assignment;
import com.unir.tfm.gestion_fisioterapeutas.model.User;
import com.unir.tfm.gestion_fisioterapeutas.repository.AssignmentRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

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
            //System.out.println("El contexto de seguridad no contiene autenticación.");
            throw new IllegalStateException("El contexto de seguridad no contiene autenticación.");
        }

        //System.out.println("Tipo de autenticación: " + authentication.getClass().getName());
        //System.out.println("Detalles de autenticación: " + authentication);

        if (authentication instanceof UsernamePasswordAuthenticationToken authToken) {
            Object details = authToken.getDetails();

            if (details instanceof String token) {
                //System.out.println("Token encontrado en el contexto de seguridad: " + token);
                return token;
            } else {
                System.out.println("Los detalles de autenticación no contienen el token esperado.");
                throw new IllegalStateException("Los detalles de autenticación no contienen el token.");
            }
        }

        throw new IllegalStateException("La autenticación no es de tipo UsernamePasswordAuthenticationToken.");
    }

    @Override
    public Assignment assignPatientToPhysiotherapist(Long patientId, Long physiotherapistId) {
        System.out.println("Entrando a assignPatientToPhysiotherapist");
        // Obtener el token JWT del contexto de seguridad
        String token = obtenerTokenActual();
        System.out.println("Tokennn: " + token);
        if (token == null) {
            throw new IllegalArgumentException("Token no encontrado o inválido");
        }

        System.out.println("Tokennn: " + token);

        // Verificar si los usuarios son válidos
        User patient = usersFacade.getUser(patientId, token);
        System.out.println("Roll paco: " + patient.getRole());
        User physiotherapist = usersFacade.getUser(physiotherapistId, token);
        System.out.println("Roll fisio: " + physiotherapist.getRole());


        // Comprobar si los IDS corresponden a usuarios válidos
        if (patient == null || !patient.getRole().equals("patient") ||
                physiotherapist == null || !physiotherapist.getRole().equals("physiotherapist")) {
            throw new IllegalArgumentException("Invalid user ID or role");
        }

        // Validar si ya existe una asignación
        if (assignmentRepository.existsByPatientIdAndPhysiotherapistId(patientId, physiotherapistId)) {
            throw new IllegalArgumentException("Assignment already exists");
        }

        // crear y guardar la asignación
        Assignment assignment = new Assignment();
        assignment.setPhysiotherapistId(physiotherapistId);
        assignment.setPatientId(patientId);
        return assignmentRepository.save(assignment);
    }

    @Override
    public List<Assignment> getAssignedPatients(Long physiotherapistId) {
        return assignmentRepository.findByPhysiotherapistId(physiotherapistId);
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
    public boolean existsAssignment(Long patientId, Long physiotherapistId) {
        return assignmentRepository.existsByPatientIdAndPhysiotherapistId(patientId, physiotherapistId);
    }

    @Override
    public List<User> getPatients() {
        return usersFacade.getUsersByRole("patient");
    }

    @Override
    public List<User> getPhysiotherapists() {
        return usersFacade.getUsersByRole("physiotherapist");
    }

    @Override
    public void ensureAllPhysiotherapistsHavePatients() {
        List<User> physiotherapists = getPhysiotherapists();
        physiotherapists.forEach(physiotherapist -> {
            Long physiotherapistId = physiotherapist.getId();
            if (assignmentRepository.findByPhysiotherapistId(physiotherapistId).isEmpty()) {
                throw new IllegalStateException(
                        String.format("Physiotherapist with ID %d has no assigned patients.", physiotherapistId));
            }
        });
    }

}
