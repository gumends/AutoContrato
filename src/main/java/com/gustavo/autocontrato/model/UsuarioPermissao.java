package com.gustavo.autocontrato.model;

public enum UsuarioPermissao {

    ADMIN("admin"),

    USER("user");

    private String permossao;

    UsuarioPermissao(String role){
        this.permossao = role;
    }

    public String getPermissao() {
        return permossao;
    }
}
