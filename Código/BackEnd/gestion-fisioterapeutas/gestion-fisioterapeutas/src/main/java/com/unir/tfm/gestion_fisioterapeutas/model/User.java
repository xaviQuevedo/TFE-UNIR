package com.unir.tfm.gestion_fisioterapeutas.model;

import lombok.Data;


@Data
public class User {
    private Long user_id;
    private String email;
    private String role;
    private String name;
    private String lastname;

}
