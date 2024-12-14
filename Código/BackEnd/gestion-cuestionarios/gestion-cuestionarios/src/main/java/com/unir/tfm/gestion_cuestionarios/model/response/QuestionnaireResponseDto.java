package com.unir.tfm.gestion_cuestionarios.model.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireResponseDto {
    private Long id;
    private String title;
    private String description;
    private List<QuestionResponseDto> questions;

}
