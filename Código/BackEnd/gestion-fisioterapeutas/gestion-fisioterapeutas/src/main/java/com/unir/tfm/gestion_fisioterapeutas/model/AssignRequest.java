package com.unir.tfm.gestion_fisioterapeutas.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssignRequest {
    private Long physiotherapistId;
    private List<Long> patientIds;

}
