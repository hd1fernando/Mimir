package com.maquinadebusca.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

@Entity
@JsonIdentityInfo (
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Documento implements Serializable {

    static final long serialVersionUID = 1L;
    
  @Id
  @GeneratedValue (strategy = GenerationType.AUTO)
  private Long id;

  @NotBlank
  private String url;

  @Lob
  @NotBlank
  private String texto;

  @Lob
  @NotBlank
  private String visao;

  private double frequenciaMaxima;

  private double somaQuadradosPesos;

  @OneToMany (
          mappedBy = "documento", // Nome do atributo na classe IndiceInvertido.
          cascade = CascadeType.ALL,
          fetch = FetchType.LAZY,
          orphanRemoval = true
  )
  private List<IndiceInvertido> indiceInvertido;

    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "documento_link",
            joinColumns = @JoinColumn(name = "documento_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "link_id", referencedColumnName = "id"))
    private Set<Link> links;

    @OneToOne(
            mappedBy = "documento",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = false
    )
    private Host host;

    public Documento() {
        //links = new HashSet<>();
        this.indiceInvertido = new LinkedList<>();
    }

    public Documento(String url, String texto, String visao) {
        this.url = url;
        this.texto = texto;
        this.visao = visao;
        links = new HashSet<>();
    }

    public Set<Link> getLinks() {
        return links;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getVisao() {
        return visao;
    }

    public void setVisao(String visao) {
        this.visao = visao;
    }

    public double getFrequenciaMaxima() {
        return frequenciaMaxima;
    }

    public void setFrequenciaMaxima(double frequenciaMaxima) {
        this.frequenciaMaxima = frequenciaMaxima;
    }

    public double getSomaQuadradosPesos() {
        return somaQuadradosPesos;
    }

    public void setSomaQuadradosPesos(double somaQuadradosPesos) {
        this.somaQuadradosPesos = somaQuadradosPesos;
    }

    public List<IndiceInvertido> getIndiceInvertido() {
        return indiceInvertido;
    }

    public void setIndiceInvertido(List<IndiceInvertido> indiceInvertido) {
        this.indiceInvertido = indiceInvertido;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public void addLink(Link link) {
        this.links.add(link);
    }

    public void removeLink(Link link) {
        links.remove(link);
    }

    public void addHost(Host host) {
        host.setDocumento(this);
        this.host = host;
    }

    public void inserirTermo(TermoDocumento termo) {
        // Cria uma nova entrada para o índice invertido com o termo informado como parâmetro e com o documento corrente.
        IndiceInvertido entradaIndiceInvertido = new IndiceInvertido(termo, this);
        // Insere a nova entrada no índice invertido do documento corrente.
        this.indiceInvertido.add(entradaIndiceInvertido);
        // Insere a nova entrada no índice invertido do termo que foi informado como parâmetro.
        termo.getIndiceInvertido().add(entradaIndiceInvertido);
    }

    public void removeTermo(TermoDocumento termo) {
        Iterator<IndiceInvertido> iterator = this.indiceInvertido.iterator();
        while (iterator.hasNext()) {
            IndiceInvertido entradaIndiceInvertido = iterator.next();
            if (entradaIndiceInvertido.getTermo().equals(termo) && entradaIndiceInvertido.getDocumento().equals(this)) {
                // Remoção no Banco de Dados a partir da tabela Documento.
                iterator.remove();
                // Remoção no Banco de Dados a partir da tabela TermoDocumento.
                entradaIndiceInvertido.getTermo().getIndiceInvertido().remove(entradaIndiceInvertido);
                // Remoção na memória RAM.
                entradaIndiceInvertido.setDocumento(null);
                entradaIndiceInvertido.setTermo(null);
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.url);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Documento other = (Documento) obj;
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
