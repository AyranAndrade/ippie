package br.com.ippie.negocio;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import static br.com.ippie.negocio.Configuracao.*;
import java.util.ArrayList;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.AssertTrue;

/**
 *
 * @author Ayran
 */
@Entity
@Table(name="conteudo")
public class Conteudo implements Serializable 
{
@Id
@GeneratedValue
@Column(name="cd_conteudo")
private long codigo;

@Column(name="nm_conteudo",length=1000)
@Size(max=1000,message="O conteúdo não pode ultrapassar 1000 caracteres!")
private String texto;

@ManyToMany(fetch=FetchType.EAGER)
@JoinTable(name="item_conteudo_assunto",joinColumns=@JoinColumn(name="cd_conteudo"),
inverseJoinColumns=@JoinColumn(name="cd_assunto"))
@NotNull(message="A lista de assuntos de um conteúdo não pode ser nula!")
@Size(min=minimoDeAssuntosPorConteudo,max=maximoDeAssuntosPorConteudo,message="O conteúdo deve ter, "
        + "pelo menos, "+minimoDeAssuntosPorConteudo+" assuntos e, no máximo, "
        +maximoDeAssuntosPorConteudo+" assuntos.")
private Collection<Assunto> assuntos;

@OneToMany(orphanRemoval=true)
@JoinColumn(name="cd_conteudo",nullable=false)
private Collection<Comentario> comentarios;

@Column(name="dt_conteudo",nullable=false)
@NotNull(message="O momento da criação de um conteúdo não pode ser nulo!")
private LocalDateTime dataCriacao;

@Column(name="nm_url_link",length=1999)
@Size(max=1999,message="O link deve ter, no máximo, 1999 caracteres.")
private String urlLink;

@Column(name="nm_url_video")
@Size(max=255,message="A url do vídeo deve ter, no máximo, 255 caracteres.")
private String urlVideo;

@Transient
private String extensaoVideo;

@Column(name="nm_url_imagem")
@Size(max=255,message="A url da imagem deve ter, no máximo, 255 caracteres.")
private String urlImagem;

@ManyToOne
@JoinColumn(name="cd_autor",nullable=false)
@NotNull(message="O autor do conteúdo não pode ser nulo!")
private Usuario autor;

@Column(name="ic_ativo")
@AssertTrue
private boolean ativo;

@Transient
private Collection<Usuario> usuariosMarcados;

@ManyToOne
@JoinColumn(name="cd_tipo_conteudo")
@NotNull(message="O tipo do conteúdo não pode ser nulo!")
private TipoConteudo tipo;

@OneToMany(orphanRemoval=true)
@JoinColumn(name="cd_conteudo",nullable=false)
private Collection<Pesame> pesames;

@OneToMany(orphanRemoval=true)
@JoinColumn(name="cd_conteudo")
private Collection<Aprovacao> aprovacoes;

@OneToMany(orphanRemoval=true)
@JoinColumn(name="cd_conteudo")
private Collection<Reprovacao> reprovacoes;

@OneToMany(orphanRemoval=true)
@JoinColumn(name="cd_conteudo",nullable=false)
private Collection<Concordar> concordar;

@OneToMany(orphanRemoval=true)
@JoinColumn(name="cd_conteudo",nullable=false)
private Collection<Discordar> discordar;

    public Conteudo()
    {
    inicia();
    }
    
    private void inicia()
    {
    assuntos=new ArrayList<>();
    comentarios=new ArrayList<>();
    usuariosMarcados=new ArrayList<>();
    pesames=new ArrayList<>();
    aprovacoes=new ArrayList<>();
    reprovacoes=new ArrayList<>();
    concordar=new ArrayList<>();
    discordar=new ArrayList<>();
    ativo=true;
    }

    public Collection<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Collection<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public Collection<Pesame> getPesames() {
        return pesames;
    }

    public void setPesames(Collection<Pesame> pesames) {
        this.pesames = pesames;
    }

    public Collection<Aprovacao> getAprovacoes() {
        return aprovacoes;
    }

    public void setAprovacoes(Collection<Aprovacao> aprovacoes) {
        this.aprovacoes = aprovacoes;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Collection<Reprovacao> getReprovacoes() {
        return reprovacoes;
    }

    public void setReprovacoes(Collection<Reprovacao> reprovacoes) {
        this.reprovacoes = reprovacoes;
    }

    public Collection<Concordar> getConcordar() {
        return concordar;
    }

    public void setConcordar(Collection<Concordar> concordar) {
        this.concordar = concordar;
    }

    public Collection<Discordar> getDiscordar() {
        return discordar;
    }

    public void setDiscordar(Collection<Discordar> discordar) {
        this.discordar = discordar;
    }
    
    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Collection<Assunto> getAssuntos() {
        return assuntos;
    }

    public void setAssuntos(Collection<Assunto> assuntos) {
        this.assuntos = assuntos;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) 
    {
    this.urlVideo=urlVideo;
      if(urlVideo!=null)
      {
      extensaoVideo=urlVideo.substring(urlVideo.lastIndexOf(".")+1);
      }
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Collection<Usuario> getUsuariosMarcados() {
        return usuariosMarcados;
    }

    public void setUsuariosMarcados(Collection<Usuario> usuariosMarcados) {
        this.usuariosMarcados = usuariosMarcados;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.dataCriacao);
        hash = 89 * hash + Objects.hashCode(this.autor);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Conteudo other = (Conteudo) obj;
      if(other.codigo!=0 && this.codigo!=0)
      {
      return other.codigo==this.codigo;
      }
        if (!Objects.equals(this.dataCriacao, other.dataCriacao)) {
            return false;
        }
    return Objects.equals(this.autor, other.autor);
    }

    public TipoConteudo getTipo() {
        return tipo;
    }

    public void setTipo(TipoConteudo tipo) {
        this.tipo = tipo;
    }

    public String getExtensaoVideo() {
        return extensaoVideo;
    }

    public void setExtensaoVideo(String extensaoVideo) {
        this.extensaoVideo = extensaoVideo;
    }
    
    public String dataEHoraFormatada()
    {
      if(dataCriacao!=null)
      {
      return (dataCriacao.getHour()<10 ? "0"+dataCriacao.getHour() : dataCriacao.getHour())
              +":"+
              (dataCriacao.getMinute()<10 ? "0"+dataCriacao.getMinute() : dataCriacao.getMinute())
              +" "+
              (dataCriacao.getDayOfMonth()<10 ? "0"+dataCriacao.getDayOfMonth() : dataCriacao.getDayOfMonth())
              +"/"+
              (dataCriacao.getMonthValue()<10 ? "0"+dataCriacao.getMonthValue() : dataCriacao.getMonthValue())
              +"/"+dataCriacao.getYear();
      }
    return null;
    }
}