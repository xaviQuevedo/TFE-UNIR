package com.unir.tfm.gestion_cuestionarios.model.entity;

import lombok.Data;

@Data
public class User {
    private Long user_id;
    private String name;
    private String email;
    private String role;
}
