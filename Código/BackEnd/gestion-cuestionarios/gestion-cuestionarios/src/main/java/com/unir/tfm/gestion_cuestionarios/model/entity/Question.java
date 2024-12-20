package com.unir.tfm.gestion_cuestionarios.model.entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "questions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "question_text")
    private String text;

    @Column(name = "question_type")
    private String type;

    // Cambiar el tipo a String para reflejar el almacenamiento de JSON o texto en la base de datos.
    @Column(name = "options", columnDefinition = "TEXT")
    private String options;

    @ManyToOne
    @JoinColumn(name = "questionnaire_id")
    private Questionnaire questionnaire;

}
