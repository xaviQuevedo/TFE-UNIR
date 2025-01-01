package com.unir.tfm.gestion_cuestionarios.service.ClinicalHistory;

import com.unir.tfm.gestion_cuestionarios.model.response.ClinicalHistoryDto;

public interface ClinicalHistoryService {
    ClinicalHistoryDto generateClinicalHistory(Long patientId);

    byte[] generateClinicalHistoryPdf(Long patientId);

}
