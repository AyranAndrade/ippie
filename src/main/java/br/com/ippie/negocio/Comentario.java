package br.com.ippie.negocio;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ayran
 */
@Entity
@Table(name="comentario")
public class Comentario implements Serializable 
{
@Id
@GeneratedValue
@Column(name="cd_comentario")
private long codigo;

@ManyToOne(optional=false)
@JoinColumn(name="cd_autor")
@NotNull(message="O autor do comentário não pode ser nulo!")
private Usuario autor;

@Column(name="dt_comentario",nullable=false)
@NotNull(message="O momento da criação de um comentário não pode ser nulo!")
private LocalDateTime dataCriacao;

@Column(name="nm_comentario",length=200)
@Size(max=200,message="O tamanho máximo de um comentário é 200 caracteres!")
private String texto;

@Column(name="nm_url_imagem")
@Size(max=255,message="O comprimento da url é de, no máximo, 255 caracteres!")
private String urlImagem;

@Column(name="ic_ativo")
@AssertTrue
private boolean ativo;

@OneToMany
@JoinColumn(name="cd_comentario")
private Collection<Aprovacao> aprovacoes;
@OneToMany
@JoinColumn(name="cd_comentario")
private Collection<Reprovacao> reprovacoes;

    public Comentario() 
    {
    inicia();
    }
    
    private void inicia()
    {
    aprovacoes=new HashSet<>();
    reprovacoes=new HashSet<>();
    ativo=true;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public Collection<Aprovacao> getAprovacoes() {
        return aprovacoes;
    }

    public void setAprovacoes(Collection<Aprovacao> aprovacoes) {
        this.aprovacoes = aprovacoes;
    }

    public Collection<Reprovacao> getReprovacoes() {
        return reprovacoes;
    }

    public void setReprovacoes(Collection<Reprovacao> reprovacoes) {
        this.reprovacoes = reprovacoes;
    }
}