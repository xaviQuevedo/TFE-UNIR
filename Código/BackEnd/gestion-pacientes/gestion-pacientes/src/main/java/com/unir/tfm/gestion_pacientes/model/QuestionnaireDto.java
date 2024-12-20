package com.unir.tfm.gestion_pacientes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireDto {
    private Long id;
    private String title;
    private String description;
}
