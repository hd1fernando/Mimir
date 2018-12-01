/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maquinadebusca.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Termo implements Serializable {

    static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String texto;

    private Long n;

    @OneToMany(
            mappedBy = "termo", //esse é o nome do atributo na classe IndiceInvertivo
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<IndiceInvertido> indiceInvertido;

    public Termo() {
        indiceInvertido = new LinkedList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Long getN() {
        return n;
    }

    public void setN(Long n) {
        this.n = n;
    }

    public List<IndiceInvertido> getIndiceInvertido() {
        return indiceInvertido;
    }

    public void setIndiceInvertido(List<IndiceInvertido> indiceInvertido) {
        this.indiceInvertido = indiceInvertido;
    }

    public void inserirEntradaIndiceInvertido(Documento documento, int frequencia) {
        // Cria uma nova entrada para o índice invertido com o termo corrente, o documento informado como parâmetro e a frequencia do termo no documento.
        IndiceInvertido entradaIndiceInvertido = new IndiceInvertido(this, documento, frequencia);
        // Insere a nova entrada no índice invertido do termo corrente.
        this.indiceInvertido.add(entradaIndiceInvertido);
        // Insere a nova entrada no índice invertido do documento que foi informado como parâmetro.
        documento.getIndiceInvertido().add(entradaIndiceInvertido);
    }

    public void removeDocumento(Documento documento) {
        Iterator<IndiceInvertido> iterator = this.indiceInvertido.iterator();
        while (iterator.hasNext()) {
            IndiceInvertido entradaIndiceInvertido = iterator.next();
            if (entradaIndiceInvertido.getTermo().equals(this) && entradaIndiceInvertido.getDocumento().equals(documento)) {
                // Remoção no Banco de Dados a partir da tabela Termo.
                iterator.remove();
                // Remoção no Banco de Dados a partir da tabela Documento.
                entradaIndiceInvertido.getDocumento().getIndiceInvertido().remove(entradaIndiceInvertido);
                entradaIndiceInvertido.setDocumento(null);
                entradaIndiceInvertido.setTermo(null);
            }
        }
    }

    public void setFrequencia(int frequencia, Documento documento) {
        Iterator<IndiceInvertido> iterator = this.indiceInvertido.iterator();
        while (iterator.hasNext()) {
            IndiceInvertido entradaIndiceInvertido = iterator.next();
            if (entradaIndiceInvertido.getTermo().equals(this) && entradaIndiceInvertido.getDocumento().equals(documento)) {
                entradaIndiceInvertido.setFrequencia(frequencia);
                break;
            }
        }
    }

    public void setFrequenciaNormalizada(Documento documento) {
        Iterator<IndiceInvertido> iterator = this.indiceInvertido.iterator();
        while (iterator.hasNext()) {
            IndiceInvertido entradaIndiceInvertido = iterator.next();
            if (entradaIndiceInvertido.getTermo().equals(this) && entradaIndiceInvertido.getDocumento().equals(documento)) {
                entradaIndiceInvertido.setFrequenciaNormalizada(entradaIndiceInvertido.getFrequencia() / documento.getFrequenciaMaxima());
                break;
            }
        }
    }

    public void setPeso(double peso, Documento documento) {
        Iterator<IndiceInvertido> iterator = this.indiceInvertido.iterator();
        while (iterator.hasNext()) {
            IndiceInvertido entradaIndiceInvertido = iterator.next();
            if (entradaIndiceInvertido.getTermo().equals(this) && entradaIndiceInvertido.getDocumento().equals(documento)) {
                entradaIndiceInvertido.setPeso(peso);
                break;
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.texto);
        return hash;
    }

    @Override
    public boolean equals(Object obj
    ) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Termo other = (Termo) obj;
        if (!Objects.equals(this.texto, other.texto)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
