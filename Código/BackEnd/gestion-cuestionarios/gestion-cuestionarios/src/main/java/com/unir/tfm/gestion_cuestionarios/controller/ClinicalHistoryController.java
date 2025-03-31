package com.unir.tfm.gestion_cuestionarios.controller;

import com.unir.tfm.gestion_cuestionarios.service.ClinicalHistory.ClinicalHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clinical-histories")
public class ClinicalHistoryController {

    @Autowired
    private ClinicalHistoryService clinicalHistoryService;

    /**
     * Endpoint para generar y obtener la historia clínica de un paciente en formato
     * JSON.
     *
     * @param patientId El ID del paciente.
     * @return La historia clínica del paciente.
     */
   /*  @GetMapping("/{patientId}")
    public ResponseEntity<ClinicalHistoryDto> getClinicalHistory(@PathVariable Long patientId) {
        try {
            ClinicalHistoryDto clinicalHistory = clinicalHistoryService.generateClinicalHistory(patientId);
            return ResponseEntity.ok(clinicalHistory);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    } */

    /**
     * Endpoint para generar y obtener la historia clínica de un paciente en formato
     * PDF.
     *
     * @param patientId El ID del paciente.
     * @return El archivo PDF de la historia clínica.
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<byte[]> getClinicalHistoryPdf(@PathVariable Long patientId) {
        try {
            byte[] pdfContent = clinicalHistoryService.generateClinicalHistoryPdf(patientId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "historia_clinica_" + patientId + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfContent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

}
