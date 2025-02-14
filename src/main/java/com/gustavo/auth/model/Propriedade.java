package com.gustavo.auth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Propriedade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String rua;

    @Column(nullable = false)
    private int numero;

    private int numCasa;

    @Column(nullable = false)
    private String bairro;

    @Column(nullable = false)
    private String cep;

    @Column(nullable = false)
    private String localizacao;

    private String aluguel;

    private String dataPagamento;

    @OneToOne(mappedBy = "propriedade", cascade = CascadeType.ALL)
    private Locatario locatario;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private Boolean status = true;

    @ManyToOne
    @JoinColumn(name = "proprietario_id")
    private Proprietario proprietario;
}
