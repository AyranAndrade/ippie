package br.com.ippie.negocio;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ayran
 */
@Entity
@Table(name="tipo_conteudo")
public class TipoConteudo implements Serializable 
{
@Id
@Column(name="cd_tipo_conteudo")
private int codigo;

@Column(name="nm_tipo_conteudo",length=30,nullable=false,unique=true)
@NotNull(message="O nome de um tipo de conteúdo não pode ser nulo!")
@Size(max=30,message="O tamanho máximo de um nome de tipo de conteúdo é de 30 caracteres.")
private String nome;

    private void iniciaCodigo(String tipo)
    {
      switch (tipo) 
      {
      case "Normal":
      codigo=1;
      break;
        
      case "Morte":
      codigo=2;
      break;
        
      case "Opiniao":
      codigo=3;
      break;
          
      default:
      throw new IllegalArgumentException("Os tipos podem ser apenas Normal, Morte e Opiniao.");
      }
    }

    public TipoConteudo() {
    }

    public TipoConteudo(String tipo) {
        iniciaCodigo(tipo);
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome)
    {
    this.nome=nome;
    iniciaCodigo(nome);
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final TipoConteudo other = (TipoConteudo) obj;
      if(other.codigo!=0 && this.codigo!=0)
      {
      return other.codigo==this.codigo;
      }        
        return Objects.equals(this.nome, other.nome);
    }
}