/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.escolalura.escolalura.models;

/**
 * Classe para representar Nota (TO).
 * @author zroz
 */
public class Nota {
    
    private Double valor;
    
    public Nota() {
    }
    
    public Nota(Double valor) {
        this.valor = valor;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
    
    
}
