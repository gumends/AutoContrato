package com.gustavo.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Proprietario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)  // Garante que o campo não seja NULL no banco de dados
    private String nome;

    @Column(nullable = false)
    private String nacionalidade;

    @Column(nullable = false)
    private String rg;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private Date nascimento;

    @Column(nullable = false)
    private Boolean status = true;

    @Column(nullable = false)
    private String userId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "proprietario", fetch = FetchType.LAZY)
    private List<Propriedade> propriedades;

}

