package br.com.ippie.bean;

import br.com.ippie.negocio.Configuracao;
import br.com.ippie.infraestrutura.GerenciadorDeImagens;
import br.com.ippie.negocio.Acesso;
import br.com.ippie.negocio.Login;
import br.com.ippie.negocio.RepositorioDeLogins;
import br.com.ippie.negocio.RepositorioDeUsuarios;
import br.com.ippie.negocio.Assunto;
import br.com.ippie.negocio.RepositorioDeAcessos;
import br.com.ippie.negocio.Usuario;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author ayran
 */
@Controller
public class CadastroBean 
{
private String nome;
private String sobrenome;
private String email;
private String senha;
private String descricao;
private final Validator validator;
private final RepositorioDeUsuarios usuarios;
private final RepositorioDeLogins logins;
private final RepositorioDeAcessos acessos;
private final Configuracao config;
private String fotoPerfil;
private String fotoCabecalho;
private CroppedImage imagemFotoPerfil;
private CroppedImage imagemFotoCabecalho;

    @Autowired
    public CadastroBean(RepositorioDeUsuarios usuarios, RepositorioDeAcessos acessos,
            RepositorioDeLogins logins, Configuracao config) 
    {
    this.usuarios=usuarios;
    this.logins=logins;
    this.acessos=acessos;
    this.config=config;
    validator=Validation.buildDefaultValidatorFactory().getValidator();
    }

    public CroppedImage getImagemFotoPerfil() {
        return imagemFotoPerfil;
    }

    public void setImagemFotoPerfil(CroppedImage imagemFotoPerfil) {
        this.imagemFotoPerfil = imagemFotoPerfil;
    }

    public CroppedImage getImagemFotoCabecalho() {
        return imagemFotoCabecalho;
    }

    public void setImagemFotoCabecalho(CroppedImage imagemFotoCabecalho) {
        this.imagemFotoCabecalho = imagemFotoCabecalho;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    /**
     * Este método verifica se o email do usuário já existe no sistema. Se já 
     * existir, redireciona para a parte 2 do caadstro, caso contrário, mostra 
     * o erro. Se o sistema chegar a chamar etse método, é sinal que os 
     * atributos que compõem a "parte social" do cadastro é válido.
     * @throws java.io.IOException
     */
    public void cadastroParte1() throws IOException
    {
      if(logins.existeUsuario(email)!=null)
      {
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
              (FacesMessage.SEVERITY_ERROR,"Erro","Este email já existe no Ippie!"));
      }
      else
      {
      long n=usuarios.quantosUsuariosTem()-1;//Subtrai as contas de moderadores.
      long max=100;
      System.out.println("Usuarios no Ippie: "+n);
        if(n<max)
        {
        Usuario u=new Usuario();
        Login l=new Login();
        l.setEmail(email);
        l.setSenha(senha);
        l.setSenha(l.geraSenhaCriptografada());
        u.setLogin(l);
        u.setNome(nome);
        u.setSobrenome(sobrenome);
        ((HttpSession)FacesContext.getCurrentInstance()
              .getExternalContext().getSession(false)).setAttribute("cadastro",u);
        email=null;
        senha=null;
        nome=null;
        sobrenome=null;
        FacesContext.getCurrentInstance().getExternalContext()
              .redirect("cadastro_parte_2.xhtml");
        }
        else
        {
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
              (FacesMessage.SEVERITY_INFO,"O Ippie ainda é beta!","O Ippie "
                      + "ainda está em fase beta, e por isso, limitamos o nosso"
                      + " número de usuários a "+max+". Tenha paciência, pois "
                      + "em breve aumentaremos a nossa capacidade!"));    
        }
      }
    }
    
    private boolean usuarioPulouAlgumaParteDoCadastro()
    {
    return nome==null || nome.trim().equals("") || 
           sobrenome==null || sobrenome.trim().equals("") ||
           email==null || email.trim().equals("") || 
           senha==null || senha.trim().equals("");
    }
    
    /**
     * Valida a quantidade de assuntos favoritos. Se houver algum erro, o 
     * usuário é informado. Caso contrário, cadastra o usuário efetivamente no 
     * sistema.
     * @param assuntosFavoritos
     * @throws java.io.IOException
     */
    @Transactional
    public void cadastroParte2(Collection<Assunto> assuntosFavoritos) throws IOException
    {
      if(usuarioPulouAlgumaParteDoCadastro())
      {
      FacesContext.getCurrentInstance().getExternalContext()
              .redirect("login.xhtml");
      }
      else
      {
        if(assuntosFavoritos!=null && assuntosFavoritos.size()>=config.minimoDeAssuntosFavoritos())
        {
        Usuario u=(Usuario)((HttpSession)FacesContext.getCurrentInstance()
              .getExternalContext().getSession(false)).getAttribute("cadastro");
        u.setAssuntosFavoritos(assuntosFavoritos);
        usuarios.adiciona(u);
        Acesso a=new Acesso(LocalDateTime.now(),u);
        acessos.adiciona(a);
        u.setAcesso(a);
        ((HttpSession)FacesContext.getCurrentInstance()
              .getExternalContext().getSession(false)).removeAttribute("cadastro");
        ((HttpSession)FacesContext.getCurrentInstance()
              .getExternalContext().getSession(false)).setAttribute("usuario",u);
        assuntosFavoritos=null;
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("restrito/rolDeConteudos.xhtml");
        limpaCadastroParte1();
        }
        else
        {
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
              (FacesMessage.SEVERITY_ERROR,"Erro","Você deve escolher, pelo "
                      + "menos, "+config.minimoDeAssuntosFavoritos()+" assuntos."));    
        }
      }
    }
    
    private void limpaCadastroParte1()
    {
    nome=null;
    sobrenome=null;
    email=null;
    senha=null;
    RequestContext.getCurrentInstance().update("frmLoginCadastro");
    }
    
    /**
     * Modifica a descrição do usuário atualmente logado.
     */
    @Transactional
    public void editaDescricao()
    {
    usuarios.editaDescricao(usuario());
    }
    
    /**
     * Edita o cadastro básico do usuário.
     */
    @Transactional
    public void editaSocial()
    {
    Usuario u=usuario();
    u.setNome(nome);
    u.setSobrenome(sobrenome);
    u.setDescricao(descricao);
    Set<ConstraintViolation<Usuario>> v=validator.validate(u);
      if(v.isEmpty())
      {
      usuarios.editaSocial(u);
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_INFO,"Ippie","Cadastro editado com sucesso!"));
      }
      else
      {
      FacesContext.getCurrentInstance().addMessage(null,new FacesMessage
            (FacesMessage.SEVERITY_ERROR,"Erro",v.stream().findAny().get()
                    .getMessage()));
      }
    nome=null;
    sobrenome=null;
    descricao=null;
    }
    
    /**
     * Apenas salva a foto upada num InputStream, para que o usuário possa cortar
     * a foto mais tarde. Também redimensiona a foto, caso esta seja muito grande.
     * @param e Este argumento é passado pelo próprio Primefaces.
     */
    public void editaFotoCabecalho(FileUploadEvent e)
    {
      try 
      {
      GerenciadorDeImagens imagens=new GerenciadorDeImagens();
        if(ImageIO.read(e.getFile().getInputstream()).getHeight()>400)
        {
        fotoCabecalho=imagens.grava(e.getFile().getInputstream(),400,config.fotoCabecalhoAbsoluto());    
        }
        else if(ImageIO.read(e.getFile().getInputstream()).getHeight()<270)
        {
        fotoCabecalho=imagens.grava(e.getFile().getInputstream(),300,config.fotoCabecalhoAbsoluto());
        }
        else
        {
        fotoCabecalho=imagens.grava(e.getFile().getInputstream(),config.fotoCabecalhoAbsoluto());
        }
      }
      catch (IOException ex) 
      {
      Logger.getLogger(CadastroBean.class.getName()).log(Level.SEVERE,"Problema"
              + " na hora de editar a foto de cabeçalho do usuario.", ex);
      }
    }
    
    /**
     * Efetivamente edita a foto de cabaçalho do usuário, salvando-a no sistema.
     * Esta foto já está cortada, o que não acontece no método editaFotoCabecalho.
     */
    @Transactional
    public void salvaFotoCabecalho()
    {
      if(imagemFotoCabecalho!=null)
      {
          try
          {
          GerenciadorDeImagens imagens=new GerenciadorDeImagens();
          imagens.gravaComEsteNome(new ByteArrayInputStream(imagemFotoCabecalho.getBytes()),
                  config.alturaFotoCabecalho(),fotoCabecalho,config.fotoCabecalhoAbsoluto());
          Usuario u=usuario();
          u.setFotoCabecalho(fotoCabecalho);
          usuarios.editaFotoCabecalho(u);
          anulaFotoCabecalho(null);
          }
          catch (IOException ex) 
          {
          Logger.getLogger(CadastroBean.class.getName()).log(Level.SEVERE, null, ex);
          }
      }        
    }
    
    /**
     * Apenas salva a foto upada num InputStream, para que o usuário possa cortar
     * a foto mais tarde. Também redimensiona a foto, caso esta seja muito grande.
     * @param e Este argumento é passado pelo próprio Primefaces.
     */
    public void editaFotoPerfil(FileUploadEvent e)
    {
      try 
      {
      GerenciadorDeImagens imagens=new GerenciadorDeImagens();
        if(ImageIO.read(e.getFile().getInputstream()).getHeight()>400)
        {      
        fotoPerfil=imagens.grava(e.getFile().getInputstream(),400,config.fotoPerfilAbsoluto());
        }
        else
        {
        fotoPerfil=imagens.grava(e.getFile().getInputstream(),config.fotoPerfilAbsoluto());    
        }
      }
      catch (IOException ex) 
      {
      Logger.getLogger(CadastroBean.class.getName()).log(Level.SEVERE,"Problema"
              + " na hora de editar a foto de perfil do usuario.", ex);
      }
    }
    
    /**
     * Efetivamente muda a foto de perfil do usuário, salvando-a no sistema.
     * Esta foto já está cortada, o que não acontece no método editaFotoPerfil.
     */
    @Transactional
    public void salvaFotoPerfil()
    {
      if(imagemFotoPerfil!=null)
      {
          try
          {
          GerenciadorDeImagens imagens=new GerenciadorDeImagens();
          imagens.gravaComEsteNome(new ByteArrayInputStream(imagemFotoPerfil.getBytes()),
                  config.alturaFotoPerfil(),fotoPerfil,config.fotoPerfilAbsoluto());
          Usuario u=usuario();
          u.setFotoPerfil(fotoPerfil);
          usuarios.editaFotoPerfil(u);
          anulaFotoPerfil(null);
          }
          catch (IOException ex) 
          {
          Logger.getLogger(CadastroBean.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
    }
    
    private Usuario usuario()
    {
    return (Usuario)((HttpSession)FacesContext.getCurrentInstance()
              .getExternalContext().getSession(false)).getAttribute("usuario");
    }
    
    public void anulaFotoPerfil(CloseEvent event)
    {
    imagemFotoPerfil=null;
    fotoPerfil=null;
    }
    
    public void anulaFotoCabecalho(CloseEvent event)
    {
    imagemFotoCabecalho=null;
    fotoCabecalho=null;
    }
}