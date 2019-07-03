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

/**
 *
 * @author ayran
 */
@Entity
@Table(name="concordar")
public class Concordar implements Serializable
{
@Id
@GeneratedValue
@Column(name="cd_concordar")
private long codigo;

@ManyToOne(optional=false)
@JoinColumn(name="cd_autor")
@NotNull(message="O autor do Concordar não pode ser nulo!")
private Usuario autor;

@Column(name="dt_concordar",nullable=false)
@NotNull(message="O momento da criação do Concordar não pode ser nulo!")
private LocalDateTime dataCriacao;

    public Concordar() {
    }

    public Concordar(Usuario autor) {
        this.autor = autor;
        dataCriacao=LocalDateTime.now();
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor)
    {
    this.autor=autor;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.autor);
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
        final Concordar other = (Concordar) obj;
      if(other.codigo!=0 && this.codigo!=0)
      {
      return other.codigo==this.codigo;
      }        
        return Objects.equals(this.autor, other.autor);
    }
 
}