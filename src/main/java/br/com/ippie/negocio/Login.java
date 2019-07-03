package br.com.ippie.negocio;

import br.com.ippie.seguranca.BCrypt;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ayran
 */
@Entity
@Table(name="login")
public class Login implements Serializable 
{
@Id
@GeneratedValue
@Column(name="cd_login")
private long codigo;

@Column(name="nm_email",nullable=false,unique=true,length=254)
@NotNull(message="O email não pode ser nulo!")
@Size(min=3,max=254,message="O email deve ter entre 3 e 254 caracteres!")
private String email;

@Column(name="nm_senha",nullable=false,columnDefinition="char(60)")
@NotNull(message="A senha não pode ser nula!")
private String senha;//bcrypt suporta no máximo 50 caracteres, vou colocar um minimo de 8 caracteres

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) 
    {
    this.senha=senha;
    }
    
    public String getSenha()
    {
    return senha;
    }
    
    /**
     * Compara se a senha passada como argumento e igual a senha guardada no 
     * atributo senha desta mesma classe.
     * @param senha A senha ja criptografada.
     * @return true, se forem iguais, ou false, se forem diferentes,
     */
    public boolean senhasSaoIguais(String senha)
    {
      if(senha==null || !senha.startsWith("$2a$"))
      {
      //throw new java.lang.IllegalArgumentException("A senha passada não é válida!");
      return false;
      }
    return BCrypt.checkpw(this.senha,senha);
    }
    
    public String geraSenhaCriptografada()
    {
    final int forcaCriptografia=12;
    String salt=BCrypt.gensalt(forcaCriptografia);
    return BCrypt.hashpw(senha,salt);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.email);
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
        final Login other = (Login) obj;
    return Objects.equals(this.email, other.email);
    }
}