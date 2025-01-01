package com.unir.tfm.gestion_cuestionarios.service.ClinicalHistory;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.unir.tfm.gestion_cuestionarios.data.PatientQuestionnaireRepository;
import com.unir.tfm.gestion_cuestionarios.data.QuestionnaireResponseRepository;
import com.unir.tfm.gestion_cuestionarios.model.entity.PatientQuestionnaire;
import com.unir.tfm.gestion_cuestionarios.model.response.ClinicalHistoryDto;
import com.unir.tfm.gestion_cuestionarios.model.response.QuestionnaireResponse;

@Service
public class ClinicalHistoryServiceImpl implements ClinicalHistoryService {

    @Autowired
    private PatientQuestionnaireRepository patientQuestionnaireRepository;

    @Autowired
    private QuestionnaireResponseRepository questionnaireResponseRepository;

    @Override
    public ClinicalHistoryDto generateClinicalHistory(Long patientId) {
        List<PatientQuestionnaire> completedQuestionnaires = patientQuestionnaireRepository
                .findByPatientIdAndStatus(patientId, "completed");

        if (completedQuestionnaires.isEmpty()) {
            throw new RuntimeException("No se encontraron cuestionarios completados para el paciente.");
        }

        ClinicalHistoryDto clinicalHistory = new ClinicalHistoryDto();
        clinicalHistory.setPatientId(patientId);

        List<ClinicalHistoryDto.QuestionnaireHistory> historyList = completedQuestionnaires.stream()
                .map(q -> {
                    List<QuestionnaireResponse> responses = questionnaireResponseRepository
                            .findByPatientIdAndQuestionnaireIdOrderByCreatedAtAsc(patientId,
                                    q.getQuestionnaire().getId());

                    double averageScore = responses.stream()
                            .mapToInt(r -> Integer.parseInt(r.getAnswer()))
                            .average()
                            .orElse(0);

                    return new ClinicalHistoryDto.QuestionnaireHistory(
                            q.getQuestionnaire().getTitle(),
                            Date.from(q.getAssignedAt().atZone(java.time.ZoneId.systemDefault()).toInstant()),
                            q.getUpdatedAt(),
                            q.getScore(),
                            q.getComments(),
                            averageScore);
                })
                .collect(Collectors.toList());

        clinicalHistory.setHistory(historyList);

        return clinicalHistory;
    }

    @Override
    public byte[] generateClinicalHistoryPdf(Long patientId) {
        ClinicalHistoryDto clinicalHistory = generateClinicalHistory(patientId);

        try (PDDocument document = new PDDocument(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Crear flujo de contenido
            PDPageContentStream contentStream = null;
            try {
                contentStream = new PDPageContentStream(document, page);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);

                // Formateador para las fechas
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

                // Título
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("Historia Clínica");
                contentStream.endText();

                int yPosition = 720;

                // Paciente
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Cuestionarios:");
                contentStream.endText();
                yPosition -= 20;

                // Cuestionarios
                for (ClinicalHistoryDto.QuestionnaireHistory history : clinicalHistory.getHistory()) {
                    if (yPosition < 50) {
                        // Cerrar el flujo actual y crear una nueva página
                        contentStream.close();
                        page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        yPosition = 750;
                    }

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    contentStream.newLineAtOffset(50, yPosition);
                    contentStream.showText(
                            "- " + (history.getQuestionnaireTitle() != null ? history.getQuestionnaireTitle() : "N/A"));
                    contentStream.endText();
                    yPosition -= 20;

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(50, yPosition);
                    contentStream.showText("Asignado: "
                            + (history.getAssignedAt() != null ? dateFormatter.format(history.getAssignedAt())
                                    : "N/A"));
                    contentStream.endText();
                    yPosition -= 20;

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(50, yPosition);
                    contentStream.showText("Completado: "
                            + (history.getCompletedAt() != null ? dateFormatter.format(history.getCompletedAt())
                                    : "N/A"));
                    contentStream.endText();
                    yPosition -= 20;

                    if (history.getComments() != null) {
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 12);
                        contentStream.newLineAtOffset(50, yPosition);
                        contentStream.showText("Comentarios: " + history.getComments());
                        contentStream.endText();
                        yPosition -= 20;
                    }
                }
            } finally {
                if (contentStream != null) {
                    contentStream.close();
                }
            }

            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF con Apache PDFBox", e);
        }
    }
}
