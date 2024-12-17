package com.unir.tfm.gestion_cuestionarios.model.entity;

//import java.util.List;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "questionnaires")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Questionnaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "description")
    private String description;

    /*@OneToMany(mappedBy = "questionnaire", cascade = CascadeType.ALL)
    private List<Question> questions;
     @OneToMany(mappedBy = "questionnaire", cascade = CascadeType.ALL)
    private List<PatientQuestionnaire> patientQuestionnaires; */

}
