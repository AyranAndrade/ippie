package br.com.ippie.negocio;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ayran
 */
@Entity
@Table(name="assunto")
public class Assunto implements Serializable 
{
@Id
@GeneratedValue
@Column(name="cd_assunto")
private long codigo;

@Column(name="nm_assunto",length=30,nullable=false,unique=true)
@NotNull(message="O nome do assunto não pode ser nulo!")
@Size(min=2,max=30,message="O nome do assunto deve ter entre 2 e 30 caracteres.")
private String nome;

@ManyToOne(optional=false)
@JoinColumn(name="cd_autor")
@NotNull(message="O autor do assunto não pode ser nulo!")
private Usuario autor;

@Column(name="dt_assunto",nullable=false)
@NotNull(message="O momento da criação de um assunto não pode ser nulo!")
private LocalDateTime dataCriacao;

@Column(name="nm_url_assunto",nullable=false,unique=true)
@NotNull(message="A url da imagem contida num assunto não pode nula!")
@Size(max=255,message="O tamanho máximo de uma url de assunto deve ser de 255 caracteres!")
private String urlImagem;

    public Assunto() 
    {
    inicia();
    }
    
    private void inicia()
    {
    Configuracao c=new Configuracao(Configuracao.Producao);
    urlImagem=c.fotoAssuntoDefault();
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.nome);
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
        final Assunto other = (Assunto) obj;
      if(other.codigo!=0 && this.codigo!=0)
      {
      return other.codigo==this.codigo;
      }
    return Objects.equals(this.nome, other.nome);
    }
}