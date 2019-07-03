package br.com.ippie.negocio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ayran
 */
@Entity
@Table(name="usuario")
public class Usuario implements Serializable 
{
@Id
@GeneratedValue
@Column(name="cd_usuario")
private long codigo;

@Column(name="nm_nome",length=30,nullable=false)
@NotNull(message="O nome não pode ser nulo!")
@Size(min=3,max=30,message="O nome deve ter, no mínimo, 3 letras e, no máximo, 30 letras.")
private String nome;

@Column(name="nm_sobrenome",length=30,nullable=false)
@NotNull(message="O sobrenome não pode ser nulo!")
@Size(min=3,max=30,message="O sobrenome deve ter, no mínimo, 3 letras e, no máximo, 30 letras.")
private String sobrenome;

@Column(name="nm_foto_perfil",nullable=false)
@Size(max=255,message="A URL da foto de perfil não pode ultrapassar 255 caracteres.")
private String fotoPerfil;

@Column(name="nm_foto_cabecalho",nullable=false)
@Size(max=255,message="A URL da foto de cabeçalho não pode ultrapassar 255 caracteres.")
private String fotoCabecalho;

@Column(name="nm_descricao",length=200)
@Size(max=200,message="A descrição não pode ultrapassar 200 caracteres.")
private String descricao;

@Column(name="ic_ativo",nullable=false)
private boolean ativo;

@OneToOne(cascade=CascadeType.PERSIST,orphanRemoval=true)
@JoinColumn(name="cd_login")
@NotNull(message="O login de um usuário não pode ser nulo!")
private Login login;

@ManyToMany(fetch=FetchType.EAGER)
@JoinTable(name="item_usuario_assunto",joinColumns=@JoinColumn(name="cd_usuario"),
inverseJoinColumns=@JoinColumn(name="cd_assunto"))
@NotNull(message="A lista de assuntos favoritos não pode ser nula!")
@Size(min=Configuracao.minimoDeAssuntosFavoritos,message="A lista de assuntos favoritos"
        + " deve conter, pelo menos, "+Configuracao.minimoDeAssuntosFavoritos+" assuntos.")
private Collection<Assunto> assuntosFavoritos;

@Column(name="ic_moderador",nullable=false)
private boolean moderador;

@Transient
private Acesso acesso;

    public Usuario() 
    {
    inicia();
    }
    
    private void inicia()
    {
    codigo=0L;
    ativo=true;
    moderador=false;
    Configuracao c=new Configuracao(Configuracao.Producao);
    fotoCabecalho=c.fotoCabecalhoDefault();
    fotoPerfil=c.fotoPerfilDefault();
    assuntosFavoritos=new ArrayList<>();
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

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getFotoCabecalho() {
        return fotoCabecalho;
    }

    public void setFotoCabecalho(String fotoCabecalho) {
        this.fotoCabecalho = fotoCabecalho;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Collection<Assunto> getAssuntosFavoritos() {
        return assuntosFavoritos;
    }

    public void setAssuntosFavoritos(Collection<Assunto> assuntosFavoritos) {
        this.assuntosFavoritos = assuntosFavoritos;
    }

    public boolean isModerador() {
        return moderador;
    }

    public void setModerador(boolean moderador) {
        this.moderador = moderador;
    }
    
    public String getNomeCompleto()
    {
    return nome+" "+sobrenome;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.login);
        return hash;
    }

    @Override
    public boolean equals(Object obj) 
    {
      if(obj==null) 
      {
      return false;
      }
      if(getClass()!=obj.getClass()) 
      {
      return false;
      }
    final Usuario other = (Usuario) obj;
      if(other.codigo!=0L && this.codigo!=0L)
      {
      return other.codigo==this.codigo;
      }
    return Objects.equals(this.login, other.login);
    }

    public Acesso getAcesso() {
        return acesso;
    }

    public void setAcesso(Acesso acesso) {
        this.acesso = acesso;
    }
}