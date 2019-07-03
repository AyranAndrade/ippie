package br.com.ippie.bean;

import br.com.ippie.infraestrutura.EnviadorDeEmails;
import br.com.ippie.infraestrutura.GeradorDeCodigo;
import br.com.ippie.negocio.Acesso;
import br.com.ippie.negocio.Login;
import br.com.ippie.negocio.RepositorioDeAcessos;
import br.com.ippie.negocio.RepositorioDeLogins;
import br.com.ippie.negocio.Usuario;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Ayran
 */
@Controller
public class LoginBean 
{
private String email;
private String senha;
private String novaSenha;
private String velhaSenha;
private String emailEsqueciSenha;
private int codigo;
private int codigoDigitado;
private final RepositorioDeLogins logins;
private final RepositorioDeAcessos acessos;
private final GeradorDeCodigo gerador;
private final ConteudoBean conteudoBean;

    @Autowired
    public LoginBean(RepositorioDeLogins logins, RepositorioDeAcessos acessos, 
            EnviadorDeEmails emails, ConteudoBean conteudoBean)
    {
    this.logins=logins;
    this.acessos=acessos;
    this.gerador=new GeradorDeCodigo();
    this.conteudoBean=conteudoBean;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getVelhaSenha() {
        return velhaSenha;
    }

    public void setVelhaSenha(String velhaSenha) {
        this.velhaSenha = velhaSenha;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getEmailEsqueciSenha() {
        return emailEsqueciSenha;
    }

    public void setEmailEsqueciSenha(String emailEsqueciSenha) {
        this.emailEsqueciSenha = emailEsqueciSenha;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigoDigitado() {
        return codigoDigitado;
    }

    public void setCodigoDigitado(int codigoDigitado) {
        this.codigoDigitado = codigoDigitado;
    }
    
    /**
     * Verifica se o usuario tem conta no sistema. Se tiver, registra na session
     * um atributo com chave "usuario". Se nao tiver, exibe uma mensagem de erro
     * atraves do componente growl, do Primefaces. Tambem registra o horario de
     * entrada do usuario no sistema.
     * @throws java.io.IOException
     */
    @Transactional
    public void login() throws IOException
    {
    Login l=new Login();
    l.setEmail(email);
    l.setSenha(senha);
    Usuario u=logins.existeUsuario(l);
      if(u==null)
      {
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
              (FacesMessage.SEVERITY_ERROR,"Erro","Email ou senha inválidos."));    
      }
      else
      {
      Acesso a=new Acesso(LocalDateTime.now(),u);
      acessos.adiciona(a);
      u.setAcesso(a);
      ((HttpSession)FacesContext.getCurrentInstance().getExternalContext()
              .getSession(false)).setAttribute("usuario",u);
      email=null;
      senha=null;
        if(u.isModerador())
        {
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("restrito/denuncia.xhtml");    
        }
        else
        {
        conteudoBean.redirecionaRolDeConteudosRestrito();
        }
      }
    }
    
    /**
     * Invalida a session atual e redireciona o usuario pra pagina de login, 
     * alem de registrar o horario de saida do usuario do sistema.
     * @throws java.io.IOException
     */
    @Transactional
    public void logout() throws IOException
    {
    Acesso a=usuario().getAcesso();
    a.setDataSaida(LocalDateTime.now());
    acessos.finaliza(a);
    ((HttpSession)FacesContext.getCurrentInstance().getExternalContext()
              .getSession(false)).invalidate();
    FacesContext.getCurrentInstance().getExternalContext()
                .redirect("../login.xhtml");
    }
    
    /**
     * Verifica se o emailEsqueciSenha existe no sistema. Se existir, manda um 
     * código aleatório pro email.
     */
    public void esqueciASenhaParte1()
    {
      if(logins.existeUsuario(email)!=null)
      {
      codigo=gerador.gera(5,40);
      //ENVIA EMAIL
      }
      else
      {
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
              (FacesMessage.SEVERITY_ERROR,"Erro","O email informado não "
                      + "existe no sistema."));
      }
    }
    
    /**
     * Verifica se o código digitado é igual ao enviado por email. Se for, ele
     * gera uma nova senha e a manda pro email, além de salvá-la no sistema. 
     * Caso não seja, exibe uma mensagem de erro.
     */
    @Transactional
    public void esqueciASenhaParte2()
    {
      if(codigo==codigoDigitado)
      {
      novaSenha=gerador.gera(5,40)+"";
      Login l=new Login();
      l.setEmail(email);
      l.setSenha(novaSenha);
      logins.editaSenha(l);
      //ENVIA EMAIL COM A NOVA SENHA
      }
      else
      {
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
              (FacesMessage.SEVERITY_ERROR,"Erro","O código digitado é "
                      + "diferente do código enviado por email.")); 
      }
    }
    
    /**
     * Troca a senha atual do usuario no sistema pela senha contida no atributo
     * NovaSenha, desta mesma classe. Contudo, o atributo senha deve ser 
     * preenchido com a senha atual do usuario.
     */
    @Transactional
    public void mudaSenha()
    {
    Login l=new Login();
    l.setSenha(velhaSenha);
    String senhaAntiga=null;
    Login login=logins.existeUsuario(usuario().getLogin().getEmail());
      if(login!=null)
      {
      senhaAntiga=login.getSenha();
      }
      if(novaSenha.length()<8)
      {
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
              (FacesMessage.SEVERITY_ERROR,"Erro","A senha deve conter, pelo menos, 8 caracteres!"));    
      }
      else
      {
        if(l.senhasSaoIguais(senhaAntiga))
        {
        l.setEmail(email);
        l.setSenha(novaSenha);
        logins.editaSenha(l);
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
              (FacesMessage.SEVERITY_INFO,"Ippie","Senha alterada com "
                      + "sucesso!"));
        }
        else
        {
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
              (FacesMessage.SEVERITY_ERROR,"Erro","A sua senha atual está incorreta."));
        }
      }
    velhaSenha=null;
    novaSenha=null;
    }
    
    private Usuario usuario()
    {
    return (Usuario)((HttpSession)FacesContext.getCurrentInstance()
              .getExternalContext().getSession(false)).getAttribute("usuario");
    }
}